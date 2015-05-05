package swift.antidote.operations;

import com.basho.riak.client.core.FutureOperation;
import com.basho.riak.client.core.RiakMessage;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import swift.antidote.AntidoteMessageCodes;
import swift.antidote.pb.AntidotePB;
import swift.antidote.utils.OID;

import java.util.List;

public class GetCounterOperation extends FutureOperation<Integer, AntidotePB.FpbGetCounterResp, OID> {

    private final OID oid;

    public GetCounterOperation(OID oid) {
        this.oid = oid;
    }

    @Override
    protected Integer convert(List<AntidotePB.FpbGetCounterResp> rawResponse) {
        AntidotePB.FpbGetCounterResp rr = rawResponse.get(0);
        return rr.hasValue() ? rr.getValue() : null;
    }

    @Override
    protected RiakMessage createChannelMessage() {
        return new RiakMessage(AntidoteMessageCodes.MSG_GetCounterReq, AntidotePB.FpbGetCounterReq.newBuilder().setKey(ByteString.copyFrom(oid.getBytes())).build().toByteArray());
    }

    @Override
    protected AntidotePB.FpbGetCounterResp decode(RiakMessage rawMessage) {
        try {
            return AntidotePB.FpbGetCounterResp.parseFrom(rawMessage.getData());
        }
        catch (InvalidProtocolBufferException e)
        {
            throw new IllegalArgumentException("Invalid message received", e);
        }
    }

    @Override
    public OID getQueryInfo() {
        return oid;
    }
}
