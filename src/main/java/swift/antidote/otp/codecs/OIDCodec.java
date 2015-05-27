package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import swift.antidote.otp.Erl;
import swift.core.OID;

public class OIDCodec implements ErlangCodec<OID> {

    @Override
    public OID decode(OtpErlangObject object) {
        return new OID(Erl.toString(object));
    }

    @Override
    public OtpErlangObject encode(OID object) {
        return new OtpErlangString(object.toString());
    }

}
