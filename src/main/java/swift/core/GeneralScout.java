package swift.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swift.crdt.CrdtImplementation;
import swift.crdt.Operations;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Logical scout, handles transaction storage and object materialization
 * by using the simplified scout adapter.
 */
public class GeneralScout implements Scout {

    private static final Logger log = LoggerFactory.getLogger(GeneralScout.class);
    private static final String OTID_KEY = "local";

    private Clock scoutClock = Clock.EMPTY;
    private final ScoutAdapter adapter;
    private long txnCounter = 0;
    private boolean keepRunning = true;

    private final BlockingQueue<TxnInfo> transactions = new LinkedBlockingQueue<>();
    private final BlockingQueue<TxnInfo> toCommitToDC = new LinkedBlockingQueue<>();
    private final Thread commitWorker = new Thread(this::commitWorkerLoop);
    private final Thread clockWorker = new Thread(this::clockWorkerLoop);
    //private final Cache<OID,ObjInfo> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

    public GeneralScout(ScoutAdapter adapter) {
        this.adapter = adapter;
        commitWorker.start();
        clockWorker.start();
    }

    @Override
    public Clock clock() {
        return scoutClock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T read(OID oid, Class<T> type, Clock clock) {
        Object obj = readFromDC(oid, type, clock);
        if (obj == null) return null;
        return (T) ((CrdtImplementation) obj).copy();
        // TODO: implement caching
    }

    /**
     * Reads the specified object from DC and applies the required local operations.
     * @param oid object id
     * @param type interface type
     * @param clock object dependencies
     * @return object
     */
    private Object readFromDC(OID oid, Class type, Clock clock) {
        // Fetch the object from DC
        ScoutAdapter.ObjectAndClock record = adapter.tryRead(oid, type, clock.without(OTID_KEY));
        if (record == null) return null;
        // now replay local transactions
        synchronized (this) {
            transactions // we apply local transactions
                    .stream() // in order
                    .filter(i -> i.id <= clock.get(OTID_KEY)) // only the ones that dependencies require
                    .filter(i -> i.commitTime.value > record.clock.get(i.commitTime.key)) // and we skip the ones already applied
                    .flatMap(i -> i.txn.getOperations().stream()) // extract operations from transactions
                    .filter(op -> op.getOid().equals(oid)) // filter only the operations for this object
            .forEach(op -> Operations.call(record.object, op.getMethod(), op.getArgs())); // apply operation
        }
        return record.object;
    }

    @Override
    public synchronized void commit(Transaction transaction) {
        TxnInfo entry = new TxnInfo();
        entry.txn = transaction;
        entry.id = ++txnCounter;
        try
        {
            toCommitToDC.put(entry);
            transactions.put(entry);
        }
        catch (InterruptedException e)
        {
            // This should never happen - queues are unbounded
            log.error("Failed to commit the transaction locally");
            throw new IllegalStateException(e);
        }
        scoutClock = scoutClock.with(OTID_KEY, entry.id);
    }

    @Override
    public void close() {
        try
        {
            log.info("Started the closing procedure - waiting until all transactions are committed");
            while (! transactions.isEmpty()) Thread.sleep(1000); // maybe release monitor?
            log.info("All transactions committed, closing workers");
            commitWorker.interrupt();
            clockWorker.interrupt();
            keepRunning = false;
            commitWorker.join();
            clockWorker.join();
            log.info("Workers closed. Closing scout.");
            // TODO: durably store the transaction log and scoutClock
        }
        catch (InterruptedException e)
        {
            log.error("Interrupted while closing scout");
            throw new IllegalStateException(e);
        }
    }

    /**
     * Commit worker - responsible for sending the transactions to DC and logging the commit times.
     */
    private void commitWorkerLoop() {
        log.info("Commit worker running");
        while (keepRunning) {
            TxnInfo entry;
            try {
                entry = toCommitToDC.take();
            } catch (InterruptedException e) {
                log.info("Commit worker interrupted, quitting");
                return;
            }
            log.info("Trying to push transaction to DC");
            while (entry.commitTime == null) {
                entry.commitTime = adapter.tryCommit(entry.txn, entry.id); // sleep
                if (entry.commitTime == null) {
                    try {
                        log.info("Failed to commit transaction, retrying");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.info("Commit worker interrupted, quitting");
                        return;
                    }
                }
            }
        }
    }

    /**
     * Clock worker - responsible for updating the DC clock value and discarding obsolete transactions
     */
    private void clockWorkerLoop() {
        log.info("Clock worker running");
        while (keepRunning) {
            // Fetch the new DC clock
            Clock dcClock = adapter.tryGetClock();
            if (dcClock != null) { // If received a new clock
                synchronized (this) {
                    // Just in case
                    if (! dcClock.ge(scoutClock.without(OTID_KEY)))
                        log.warn("New DC clock not higher than scout clock, DC must have crashed.");

                    // increment the scout clock
                    scoutClock = scoutClock.merge(dcClock);

                    // check if any local transactions can be safely removed
                    Iterator<TxnInfo> it = transactions.iterator();
                    while (it.hasNext()) {
                        TxnInfo info = it.next();
                        if (info.commitTime == null) break; // if transaction wasn't yet committed, break
                        if (scoutClock.ge(Clock.create(info.commitTime))) { // if the transaction is k-durable, remove it
                            it.remove();
                        } else { // the transaction is not yet k-durable, so the next ones will not be either. Break
                            break;
                        }
                    }
                }
            } else {
                log.info("Failed to retrieve new k-durable clock");
            }
            // Done, sleep and retry
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.info("Clock worker interrupted, quitting");
                return;
            }
        }
    }

    private static final class TxnInfo {
        Transaction txn;
        long id;
        Clock.Entry commitTime; // assert size() == 1
    }
}
