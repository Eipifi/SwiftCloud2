package swift.core;

import java.util.List;

public interface Transaction {
    Clock getDependencies();
    List<OperationLog> getOperations();
    <T> T read(OID oid, Class<T> type);
    void commit();

    default <T> T read(String oid, Class<T> type) {
        return read(new OID(oid), type);
    }
}
