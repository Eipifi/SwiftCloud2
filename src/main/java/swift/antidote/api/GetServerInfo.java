package swift.antidote.api;

import com.basho.riak.client.api.RiakCommand;
import com.basho.riak.client.api.commands.CoreFutureAdapter;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakFuture;
import swift.antidote.operations.GetServerInfoOperation;

public class GetServerInfo extends RiakCommand<GetServerInfoOperation.Response, Void> {
    @Override
    protected RiakFuture<GetServerInfoOperation.Response, Void> executeAsync(RiakCluster cluster) {

        RiakFuture<GetServerInfoOperation.Response, Void> coreFuture = cluster.execute(new GetServerInfoOperation());

        CoreFutureAdapter<GetServerInfoOperation.Response, Void, GetServerInfoOperation.Response, Void> future =
                new CoreFutureAdapter<GetServerInfoOperation.Response, Void, GetServerInfoOperation.Response, Void>(coreFuture) {
            @Override
            protected GetServerInfoOperation.Response convertResponse(GetServerInfoOperation.Response coreResponse) {
                return coreResponse;
            }

            @Override
            protected Void convertQueryInfo(Void coreQueryInfo) {
                return coreQueryInfo;
            }
        };
        coreFuture.addListener(future);
        return future;
    }
}
