package swift.antidote.otp.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.conv.Codec;
import swift.antidote.otp.conv.Erl2;
import swift.core.OperationLog;

public class OperationLogCodec implements Codec<OperationLog> {

    @Override
    public OperationLog decode(OtpErlangObject object) {
        throw new IllegalStateException("Not implemented / needed");
    }

    @Override
    public OtpErlangObject encode(OperationLog object) {
        return Erl2.tuple(
                Erl2.encode(object.getOid()),
                Erl2.adapter(object.getType()).type(),
                Erl2.adapter(object.getType()).encodeOp(object.getMethod(), object.getArgs())
        );
    }
}
