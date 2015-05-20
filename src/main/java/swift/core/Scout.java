package swift.core;

public interface Scout {

    /**
     * Last K-durable clock received from the DC.
     * @return clock
     */
    Clock clock();

    /**
     * Reads the CRDT object from DC (possibly through cache).
     * The object is not tracked. It is a fresh object copy and changes made to it will be local.
     * In order to push operations back to DC, a transaction must be used.
     * IMPORTANT: the object will be AT LEAST AS OLD AS the dependency requires.
     * The method does not guarantee snapshot isolation.
     * @param oid object identifier
     * @param type object interface type
     * @param clock causal dependency requirements for the object
     * @param <T> object type
     * @return object or null if read failed.
     */
    <T> T read(OID oid, Class<T> type, Clock clock);

    /**
     * Submit a transaction. It will be committed asynchronously to DC.
     * @param transaction closed transaction to be committed
     */
    void commit(Transaction transaction);


    /**
     * Starts a new transaction bound to this scout.
     * @return open transaction
     */
    default Transaction newTransaction() {
        return new NormalTransaction(this);
    }

    /**
     * Closes the client and waits until all transactions were sent.
     */
    void close();
}
