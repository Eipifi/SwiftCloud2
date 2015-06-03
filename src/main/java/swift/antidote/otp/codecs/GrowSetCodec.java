package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.Erl;
import swift.core.OperationLog;
import swift.crdt.types.GrowSet;

public class GrowSetCodec implements ErlangCrdtDecoder {
    @Override
    public OtpErlangObject erlangType() {
        return Erl.makeAtom("riak_dt_gset");
    }

    @Override
    public OtpErlangObject encodeOperation(OperationLog operation) {
        return null;
    }

    @Override
    public GrowSet decode(OtpErlangObject object) {
        return null;
    }
}
