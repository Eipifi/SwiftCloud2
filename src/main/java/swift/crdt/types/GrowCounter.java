package swift.crdt.types;


import swift.crdt.Operation;

/**
 * Interface for a grow-only counter.
 */
public interface GrowCounter {

    /**
     * Returns the currently held counter value
     * @return counter value
     */
    long value();

    /**
     * Increments the counter value by a given amount.
     * @param amount
     */
    @Operation(name = "increment")
    void increment(long amount);
}
