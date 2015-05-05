package swift.core;

import swift.core.utils.CID;
import swift.core.utils.IsolationLevel;
import swift.core.utils.VClock;

public interface Transaction {
    VClock startClock();
    IsolationLevel isolationLevel();
    <T> T getObject(CID id);
    <T> T createObject(CID id, Class<T> type);
    void commit();
}
