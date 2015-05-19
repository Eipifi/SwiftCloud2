package swift.core;

import swift.crdt.Operations;

import java.util.*;

public final class NormalTransaction implements Transaction {

    private final Map<OID, Object> instances = new HashMap<>();
    private final List<OperationLog> operations = new LinkedList<>();
    private final Clock dependencies;
    private final Scout scout;

    public NormalTransaction(Scout scout) {
        this.dependencies = scout.clock();
        this.scout = scout;
    }

    @Override
    public Clock getDependencies() {
        return dependencies;
    }

    @Override
    public List<OperationLog> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized <T> T read(OID oid, Class<T> type) {
        Object object = instances.get(oid);
        if (object == null) {
            object = scout.read(oid, type, dependencies);
            if (object != null) {
                instances.put(oid, object);
            }
        }
        return object == null ? null : Operations.proxy((T) object, type, (m, a) -> this.log(oid, m, a));
    }

    @Override
    public synchronized void commit() {
        // For security reasons the transaction should be blocked from any changes from now on.
        scout.commit(this);
    }

    private synchronized void log(OID oid, String method, Object[] args) {
        operations.add(new OperationLog(oid, method, args));
    }
}
