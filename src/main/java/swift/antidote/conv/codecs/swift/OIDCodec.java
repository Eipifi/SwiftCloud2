package swift.antidote.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.conv.Codec;
import swift.antidote.conv.Erl;
import swift.core.OID;

public class OIDCodec implements Codec<OID> {
    @Override
    public OID decode(OtpErlangObject object) {
        return new OID(Erl.decode(object, String.class));
    }

    @Override
    public OtpErlangObject encode(OID object) {
        return Erl.encode(object.toString(), String.class);
    }
}
