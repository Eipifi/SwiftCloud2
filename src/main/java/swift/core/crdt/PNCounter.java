package swift.core.crdt;

import java.util.function.Consumer;
import java.util.function.LongSupplier;

/**
 * PN Counter.
 *
 */
public class PNCounter implements LongSupplier {
    private Consumer<Operation> opSink;
    private long value = 0;

    public PNCounter(long value, Consumer<Operation> opSink) {
        this.value = value;
        this.opSink = opSink;
    }

    @Override
    public long getAsLong() {
        return value;
    }

    public synchronized void increment(long diff) {
        value += diff;
        opSink.accept(new PlaceholderOperation("counter changed by " + diff));
    }

    public synchronized void decrement(long diff) {
        increment(-diff);
    }
}
