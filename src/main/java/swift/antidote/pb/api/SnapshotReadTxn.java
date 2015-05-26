package swift.antidote.pb.api;

import com.basho.riak.client.api.RiakCommand;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakFuture;

public class SnapshotReadTxn extends RiakCommand<SnapshotReadTxn.Result, Void> {

    @Override
    protected RiakFuture<Result, Void> executeAsync(RiakCluster cluster) {
        return null;
    }

    public static class Result {

    }
}
