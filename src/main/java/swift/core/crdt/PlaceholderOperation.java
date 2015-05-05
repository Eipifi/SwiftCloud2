package swift.core.crdt;

/**
 * Placeholder type, development only.
 */
public class PlaceholderOperation implements Operation {

    private final String val;

    public PlaceholderOperation(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
