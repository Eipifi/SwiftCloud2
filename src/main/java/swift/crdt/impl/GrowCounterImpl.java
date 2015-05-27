package swift.crdt.impl;

import swift.crdt.CrdtImplementation;
import swift.crdt.Operation;
import swift.crdt.types.GrowCounter;

public class GrowCounterImpl implements GrowCounter, CrdtImplementation {

    private long value;

    public GrowCounterImpl(long value) {
        this.value = value;
    }

    @Override
    public long value() {
        return value;
    }

    @Override
    @Operation(name = "increment")
    public void increment() {
        value += 1;
    }

    @Override
    public Object copy() {
        return new GrowCounterImpl(value);
    }
}
