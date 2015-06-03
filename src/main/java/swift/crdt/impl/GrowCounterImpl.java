package swift.crdt.impl;

import swift.crdt.Operation;
import swift.crdt.types.GrowCounter;

public final class GrowCounterImpl implements GrowCounter {

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
    public void increment(long amount) {
        value += amount;
    }

}
