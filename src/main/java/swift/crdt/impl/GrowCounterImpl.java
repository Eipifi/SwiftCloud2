package swift.crdt.impl;

import swift.crdt.Operation;
import swift.crdt.types.GrowCounter;

public class GrowCounterImpl implements GrowCounter {

    private int value;

    @Override
    public int value() {
        return value;
    }

    @Override
    @Operation(name = "increment")
    public void increment() {
        value += 1;
    }
}
