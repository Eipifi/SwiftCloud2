package swift.antidote.api;

import com.basho.riak.client.api.RiakCommand;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakFuture;
import swift.antidote.operations.IncrementOperation;
import swift.antidote.utils.OID;

public class IncrementCounter extends RiakCommand<Void, Void> {

    private final int value;
    private final OID oid;

    public IncrementCounter(OID oid, int value) {
        this.oid = oid;
        this.value = value;
    }

    @Override
    protected RiakFuture<Void, Void> executeAsync(RiakCluster cluster) {
        return cluster.execute(new IncrementOperation(oid, value));
    }
}
