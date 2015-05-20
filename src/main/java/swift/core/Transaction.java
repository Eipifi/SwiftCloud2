package swift.core;

import java.util.List;

public interface Transaction {

    /**
     * The dependency clock used for this transaction
     * @return clock
     */
    Clock getDependencies();

    /**
     * Operations issued during this transaction
     * @return
     */
    List<OperationLog> getOperations();

    /**
     * Reads the object. The object is tracked, so operations performed on this
     * object will be stored in the transaction log. The transaction clock describes
     * the causal dependencies of the object. If the read fails (eg. in case of broken
     * connection), null will be returned. The user can continue or abandon the transaction
     * in any case.
     * @param oid object id
     * @param type interface type
     * @param <T> object type
     * @return object
     */
    <T> T read(OID oid, Class<T> type);

    /**
     * Commits the transaction locally to the related scout.
     * The transaction and all associated object instances become read-only.
     */
    void commit();

    default <T> T read(String oid, Class<T> type) {
        return read(new OID(oid), type);
    }
}
