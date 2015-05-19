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
    int value();

    /**
     * Increments the counter value by 1.
     */
    @Operation(name = "increment")
    void increment();
}
