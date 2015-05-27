package swift.core;

/**
 * General interface for eventually consistent causal+ storage systems.
 */
public interface ScoutAdapter {
    /**
     * Method used to query for the new k-durable clock.
     * The method will be called periodically.
     * This method can safely return null if the new clock value is not available.
     * This will however stop any meaningful progress on the client side.
     * @return new clock value
     */
    Clock tryGetClock();

    /**
     * Method used to push a transaction to storage.
     * The method will be called until the transaction is committed.
     * The underlying storage must be able to handle exactly-once delivery.
     * The scout may temporarily store the transaction in local durable storage to
     * retry upon client restart.
     * The method may return null if the commit failed (or is not guaranteed to have succeeded).
     * @param transaction transaction
     * @return clock entry indicating how the transaction is identified in storage.
     */
    Clock.Entry tryCommit(Transaction transaction, long id);

    /**
     * Method used to read objects from storage.
     *
     * @param oid object id
     * @param type interface type
     * @param dependencies dependencies
     * @return object and associated snapshot clock
     */
    ObjectAndClock tryRead(OID oid, Class type, Clock dependencies);

    class ObjectAndClock {
        public Object object;
        public Clock clock;
    }
}
