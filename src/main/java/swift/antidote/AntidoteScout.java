package swift.antidote;

import swift.core.Clock;
import swift.core.OID;
import swift.core.Scout;
import swift.core.Transaction;

public class AntidoteScout implements Scout {
    @Override
    public Clock clock() {
        return null;
    }

    @Override
    public <T> T read(OID oid, Class<T> type, Clock clock) {
        return null;
    }

    @Override
    public void commit(Transaction transaction) {

    }
}
