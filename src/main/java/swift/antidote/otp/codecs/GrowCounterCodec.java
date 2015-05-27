package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import swift.antidote.otp.Erl;
import swift.crdt.impl.GrowCounterImpl;
import swift.crdt.types.GrowCounter;

public class GrowCounterCodec implements ErlangCodec<GrowCounter> {
    @Override
    public GrowCounter decode(OtpErlangObject object) {
        OtpErlangTuple tuple = Erl.tuple(object);
        // tuple.elementAt(0) -> value
        // tuple.elementAt(1) -> actual clock
        return new GrowCounterImpl(Erl.toLong(tuple.elementAt(0)));
    }

    @Override
    public OtpErlangObject encode(GrowCounter object) {
        return null;
    }

    @Override
    public OtpErlangAtom erlangType() {
        return new OtpErlangAtom("riak_dt_gcounter");
    }
}
