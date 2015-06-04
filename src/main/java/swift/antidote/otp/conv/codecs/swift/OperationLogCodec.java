package swift.antidote.otp.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.conv.Codec;
import swift.antidote.otp.conv.Erl;
import swift.core.OperationLog;

public class OperationLogCodec implements Codec<OperationLog> {

    @Override
    public OperationLog decode(OtpErlangObject object) {
        throw new IllegalStateException("Not implemented / needed");
    }

    @Override
    public OtpErlangObject encode(OperationLog object) {
        return Erl.tuple(
                Erl.encode(object.getOid()),
                Erl.adapter(object.getType()).type(),
                Erl.adapter(object.getType()).encodeOp(object.getMethod(), object.getArgs())
        );
    }
}
