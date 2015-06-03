package swift.crdt.types;

import swift.crdt.Operation;

/**
 * Interface for a grow-only set.
 *
 * Allowed types:
 *  - integer
 *  - long
 *  - float
 *  - double
 *  - boolean
 *  - string
 */
public interface GrowSet extends Iterable<Object> {

    /**
     * Checks if the given object is already in the collection.
     * @param obj
     * @return true if object belongs to the set, false otherwise
     */
    boolean contains(Object obj);

    /**
     * Adds the element to the set.
     * @param obj
     */
    @Operation(name = "add")
    void add(Object obj);

}
