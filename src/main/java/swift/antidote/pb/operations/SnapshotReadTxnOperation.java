package swift.antidote.pb.operations;

import com.basho.riak.client.core.FutureOperation;
import com.basho.riak.client.core.RiakMessage;
import com.basho.riak.client.core.operations.Operations;
import com.google.protobuf.InvalidProtocolBufferException;
import swift.antidote.pb.AntidoteMessageCodes;
import swift.antidote.pb.AntidotePB;
import swift.antidote.pb.api.SnapshotReadTxn;

import java.util.List;

public class SnapshotReadTxnOperation extends FutureOperation<SnapshotReadTxn.Result, AntidotePB.FpbSnapshotReadTxnResp, Void> {
    @Override
    protected SnapshotReadTxn.Result convert(List<AntidotePB.FpbSnapshotReadTxnResp> rawResponse) {
        return null;
    }

    @Override
    protected RiakMessage createChannelMessage() {
        /*
        return new RiakMessage(
                AntidoteMessageCodes.MSG_SnapshotReadTxnReq,
                AntidotePB.FpbSnapshotReadTxnReq.newBuilder().
        );
        */
        return null;
    }

    @Override
    protected AntidotePB.FpbSnapshotReadTxnResp decode(RiakMessage rawMessage) {
        Operations.checkMessageType(rawMessage, AntidoteMessageCodes.MSG_SnapshotReadTxnResp);
        try {
            return AntidotePB.FpbSnapshotReadTxnResp.parseFrom(rawMessage.getData());
        } catch (InvalidProtocolBufferException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Void getQueryInfo() {
        return null;
    }
}
