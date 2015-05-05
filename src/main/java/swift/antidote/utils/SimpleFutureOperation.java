package swift.antidote.utils;

import com.basho.riak.client.core.FutureOperation;

public abstract class SimpleFutureOperation<T, U> extends FutureOperation<T, U, Void> {

    @Override
    public Void getQueryInfo() { return null; }
}
