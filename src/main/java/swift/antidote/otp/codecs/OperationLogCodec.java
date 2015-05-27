package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.Erl;
import swift.core.OperationLog;

import java.util.ArrayList;
import java.util.List;

public class OperationLogCodec implements ErlangCodec<OperationLog> {
    @Override
    public OperationLog decode(OtpErlangObject object) {
        // Not needed
        throw new IllegalStateException("Not applicable");
    }

    @Override
    public OtpErlangObject encode(OperationLog op) {
        // {update, Key, Type, {OpParam, Actor}}

        return Erl.makeTuple(
                Erl.makeAtom("update"),
                Codecs.encode(op.getOid()),
                Codecs.getDecoder(op.getType()).erlangType(),
                Erl.makeTuple(
                        encodeArgs(op),
                        Codecs.encode((long) 42)
                )
        );
    }

    private OtpErlangObject encodeArgs(OperationLog op) {
        if (op.getArgs() != null && op.getArgs().length > 0) {
            List<OtpErlangObject> list = new ArrayList<>();
            list.add(Erl.makeAtom(op.getMethod()));
            for(Object o: op.getArgs()) list.add(Codecs.encode(o));
            return Erl.makeTuple(list);
        } else {
            return Erl.makeAtom(op.getMethod());
        }
    }
}
