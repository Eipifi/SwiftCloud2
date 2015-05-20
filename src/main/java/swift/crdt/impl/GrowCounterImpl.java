package swift.crdt.impl;

import swift.crdt.CrdtImplementation;
import swift.crdt.Operation;
import swift.crdt.types.GrowCounter;

public class GrowCounterImpl implements GrowCounter, CrdtImplementation {

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

    @Override
    public Object copy() {
        GrowCounterImpl c = new GrowCounterImpl();
        c.value = value;
        return c;
    }
}
