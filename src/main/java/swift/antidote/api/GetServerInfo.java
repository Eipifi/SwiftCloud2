package swift.antidote.api;

import com.basho.riak.client.api.RiakCommand;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakFuture;
import swift.antidote.operations.GetServerInfoOperation;

public class GetServerInfo extends RiakCommand<GetServerInfoOperation.Response, Void> {
    @Override
    protected RiakFuture<GetServerInfoOperation.Response, Void> executeAsync(RiakCluster cluster) {
        return cluster.execute(new GetServerInfoOperation());
    }
}
