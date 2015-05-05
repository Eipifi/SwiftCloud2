package swift.antidote.api;

import com.basho.riak.client.api.RiakCommand;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakFuture;
import swift.antidote.operations.GetCounterOperation;
import swift.antidote.utils.OID;

public class GetCounter extends RiakCommand<Integer, Void> {

    private final OID oid;

    public GetCounter(OID oid) {
        this.oid = oid;
    }

    @Override
    protected RiakFuture<Integer, Void> executeAsync(RiakCluster cluster) {
        return cluster.execute(new GetCounterOperation(oid));
    }
}
