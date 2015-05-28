package swift.crdt.types;

import swift.crdt.Operation;

import java.util.Set;

public interface GrowSet<T> extends Set<T> {

    @Override
    @Operation(name = "add")
    boolean add(T element);

}
