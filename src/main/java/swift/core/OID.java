package swift.core;

public final class OID {

    private final String key;

    public OID(String key) {
        this.key = key;
    }

    public String toString() {
        return key;
    }

    public byte[] toBytes() {
        return key.getBytes();
    }
}
