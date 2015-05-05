package swift.antidote.utils;

public final class OID {
    private String key;

    public OID(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

    public byte[] getBytes() {
        return key.getBytes();
    }
}
