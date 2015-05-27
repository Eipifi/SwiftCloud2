package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.Erl;
import swift.crdt.impl.GrowCounterImpl;
import swift.crdt.types.GrowCounter;

public class GrowCounterCodec implements ErlangCrdtDecoder<GrowCounter> {
    @Override
    public GrowCounter decode(OtpErlangObject object) {
        return new GrowCounterImpl(Erl.toLong(object));
    }

    @Override
    public OtpErlangAtom erlangType() {
        return new OtpErlangAtom("riak_dt_gcounter");
    }
}
