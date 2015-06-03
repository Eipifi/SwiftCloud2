package swift.antidote.otp.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.conv.Codec;
import swift.antidote.otp.conv.Erl2;
import swift.core.OID;

public class OIDCodec implements Codec<OID> {
    @Override
    public OID decode(OtpErlangObject object) {
        return new OID(Erl2.decode(object, String.class));
    }

    @Override
    public OtpErlangObject encode(OID object) {
        return Erl2.encode(object.toString(), String.class);
    }
}