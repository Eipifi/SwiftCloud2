package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.Erl;
import swift.core.OperationLog;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OperationLogCodec implements ErlangCodec<OperationLog> {
    @Override
    public OperationLog decode(OtpErlangObject object) {
        // Not needed
        throw new IllegalStateException("Not applicable");
    }

    @Override
    public OtpErlangObject encode(OperationLog op) {
        return Erl.makeTuple(
                Codecs.encode(op.getOid()),
                Codecs.getDecoder(op.getType()).erlangType(),
                Codecs.getDecoder(op.getType()).encodeOperation(op)
        );
    }
}
