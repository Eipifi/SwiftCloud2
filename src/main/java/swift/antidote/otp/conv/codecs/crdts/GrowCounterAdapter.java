package swift.antidote.otp.conv.codecs.crdts;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.conv.Erl2;
import swift.antidote.otp.conv.CRDTAdapter;
import swift.crdt.impl.GrowCounterImpl;
import swift.crdt.types.GrowCounter;

public class GrowCounterAdapter implements CRDTAdapter<GrowCounter> {

    @Override
    public GrowCounter decode(OtpErlangObject object) {
        return new GrowCounterImpl(Erl2.decode(object, Long.class));
    }

    @Override
    public OtpErlangAtom type() {
        return new OtpErlangAtom("riak_dt_gcounter");
    }

    @Override
    public OtpErlangObject encodeOp(String method, Object[] args) {
        return Erl2.tuple(
                new OtpErlangAtom(method),
                Erl2.encode(args[0], Long.class)
        );
    }

}
