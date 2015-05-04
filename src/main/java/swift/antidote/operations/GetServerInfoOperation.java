package swift.antidote.operations;

import com.basho.riak.client.core.FutureOperation;
import com.basho.riak.client.core.RiakMessage;
import com.basho.riak.client.core.operations.Operations;
import com.basho.riak.protobuf.RiakMessageCodes;
import com.basho.riak.protobuf.RiakPB;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.List;

public class GetServerInfoOperation extends FutureOperation<GetServerInfoOperation.Response, RiakPB.RpbGetServerInfoResp, Void> {

    @Override
    protected Response convert(List<RiakPB.RpbGetServerInfoResp> rawResponse) {
        RiakPB.RpbGetServerInfoResp rr = rawResponse.get(0);
        Response response = new Response();
        if (rr.hasNode()) response.node = new String(rr.getNode().toByteArray());
        if (rr.hasServerVersion()) response.version = new String(rr.getServerVersion().toByteArray());
        return response;
    }

    @Override
    protected RiakMessage createChannelMessage() {
        return new RiakMessage(RiakMessageCodes.MSG_GetServerInfoReq, new byte[0]);
    }

    @Override
    protected RiakPB.RpbGetServerInfoResp decode(RiakMessage rawMessage) {
        Operations.checkMessageType(rawMessage, RiakMessageCodes.MSG_GetServerInfoResp);
        try {
            return RiakPB.RpbGetServerInfoResp.parseFrom(rawMessage.getData());
        }
        catch (InvalidProtocolBufferException e)
        {
            throw new IllegalArgumentException("Invalid message received", e);
        }
    }

    @Override
    public Void getQueryInfo() {
        return null;
    }

    public class Response {
        public String node;
        public String version;
    }
}
