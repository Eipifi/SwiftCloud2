package swift.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * General scout logic, with abstracted storage implementation.
 *
 * Functionality:
 *  - transaction queueing
 *  - LRU cache
 *  - object materialization through stored transaction operation logs
 *
 *  Abstracted:
 *  - clock updates
 *  - transaction delivery
 *
 */

public final class LogicalScout implements Scout {

    private static final Logger log = LoggerFactory.getLogger(LogicalScout.class);
    private static final int OBJECT_CACHE_SIZE = 1000;
    private static final String CLOCK_LOCAL_KEY = "local";

    private Clock clock = Clock.EMPTY;
    private final ScoutAdapter adapter;
    private final Thread clockWorker = new Thread();
    private final Thread commitWorker = new Thread(this::commitWorkerLoop);
    private final BlockingQueue<TxnInfo> txnsToRemove = new LinkedBlockingQueue<>();
    private final BlockingQueue<TxnInfo> txnsToCommit = new LinkedBlockingQueue<>();
    private final AtomicLong txnCounter = new AtomicLong(1);
    private final Cache<OID, ObjClock> cache;

    public LogicalScout(ScoutAdapter adapter) {
        this.adapter = adapter;
        cache = CacheBuilder.newBuilder().maximumSize(OBJECT_CACHE_SIZE).build();
        clockWorker.start();
        commitWorker.start();
        log.info("Started the workers");
    }

    @Override
    public Clock clock() {
        return clock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T read(OID oid, Class<T> type, Clock clock) {
        // TODO: implement
        return null;
    }

    @Override
    public void commit(Transaction transaction) {
        // Prepare the transaction log
        TxnInfo info = new TxnInfo();
        info.txn = transaction;
        info.id = txnCounter.getAndIncrement();
        // Put the transaction in log
        txnsToCommit.add(info);
        // Update the clock
        clock = clock.with(CLOCK_LOCAL_KEY, info.id);
        log.info("Transaction [txn={} id={}] committed locally.", info.txn, info.id);
    }

    @Override
    public synchronized void close() {
        try {
            log.info("Closing scout...");
            // Wait until there are no more transactions to commit
            while(! (txnsToCommit.isEmpty() && txnsToRemove.isEmpty())) Thread.sleep(1000);
            // Stop the workers
            commitWorker.interrupt();
            clockWorker.interrupt();
            commitWorker.join();
            clockWorker.join();
            // TODO: save remaining transactions to durable local storage?
        } catch (InterruptedException e) {
            log.warn("Interrupted while closing the scout");
            throw new IllegalStateException(e);
        }
    }

    /**
     * Commit worker task.
     *
     * Tries to commit transactions in order. The transactions are then moved to other queue,
     * where they await until the commit durability is confirmed.
     *
     */
    private void commitWorkerLoop() {
        while (true) {
            TxnInfo info;
            try {
                info = txnsToCommit.take();
            } catch (InterruptedException e) {
                log.info("Commit worker interrupted, quitting");
                return;
            }
            while (info.clock == null) info.clock = adapter.tryCommit(info.txn, info.id);
            assert info.clock.size() == 1;
            txnsToRemove.add(info);
        }
    }

    /**
     * Clock worker task.
     *
     * Periodically checks if a new K-durable clock value is available. If so, it checks if
     * any of the local transactions can be pruned.
     */
    private void clockWorkerLoop() {
        while (true) {
            // Try to fetch new K-durable clock
            Clock newClock = adapter.tryGetClock();
            // If we got it
            if (newClock != null) {
                // Check if the clock went back in some DC, just in case
                if (! newClock.ge(clock)) log.warn("New DC clock is not >= local clock. DC must have crashed.");
                // Increase local clock
                synchronized (this) {
                    clock = clock.merge(newClock);
                }
                // Now check if there are any transactions we can prune
                Iterator<TxnInfo> it = txnsToRemove.iterator();
                while(it.hasNext()) {
                    TxnInfo info = it.next();
                    if (clock.ge(info.clock)) { // If transaction is already part of K-durable snapshot
                        log.info("Transaction [txn={} id={}] successfully pruned");
                        synchronized (this) {
                            // indicate that the new scout clock encompasses the transaction
                            clock = clock.with(CLOCK_LOCAL_KEY, info.id);
                        }
                        // finally drop the transaction entry
                        it.remove();
                    } else { // If not, skip checking the remaining transactions
                        break;
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.info("Clock worker interrupted, quitting");
                return;
            }
        }
    }

    private static class TxnInfo {
        public Transaction txn;
        public long id;
        public Clock clock;
    }

    private static class ObjClock {
        public Object object;
        public Clock clock;
    }
}
