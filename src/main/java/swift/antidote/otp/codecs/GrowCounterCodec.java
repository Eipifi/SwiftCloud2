package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.Erl;
import swift.core.OperationLog;
import swift.crdt.impl.GrowCounterImpl;
import swift.crdt.types.GrowCounter;

import java.util.Objects;

public class GrowCounterCodec implements ErlangCrdtDecoder<GrowCounter> {
    @Override
    public GrowCounter decode(OtpErlangObject object) {
        return new GrowCounterImpl(Erl.toLong(object));
    }

    @Override
    public OtpErlangAtom erlangType() {
        return Erl.makeAtom("riak_dt_gcounter");
    }

    @Override
    public OtpErlangObject encodeOperation(OperationLog operation) {
        assert operation.getArgs().length == 1 && operation.getArgs()[0] instanceof Long;
        assert Objects.equals(operation.getMethod(), "increment");
        Long value = (Long) operation.getArgs()[0];
        return Erl.makeTuple(
                Erl.makeAtom(operation.getMethod()),
                Codecs.encode(value)
        );
    }
}
