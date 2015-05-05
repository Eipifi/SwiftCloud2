package swift.core;

import swift.core.crdt.Operation;
import swift.core.utils.CID;
import swift.core.utils.IsolationLevel;
import swift.core.utils.VClock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

public class TransactionImpl implements Transaction {

    private final VClock vClock;
    private final IsolationLevel isolationLevel;
    private final Queue<Operation> opLog = new LinkedList<>();
    private Supplier<Object> materializer;

    TransactionImpl(VClock vClock, IsolationLevel isolationLevel) {
        this.vClock = vClock;
        this.isolationLevel = isolationLevel;
    }

    @Override
    public VClock startClock() {
        return vClock;
    }

    @Override
    public IsolationLevel isolationLevel() {
        return isolationLevel;
    }

    @Override
    public <T> T getObject(CID id) {
        return null;
    }

    @Override
    public <T> T createObject(CID id, Class<T> type) {
        return null;
    }

    @Override
    public void commit() { }
}
