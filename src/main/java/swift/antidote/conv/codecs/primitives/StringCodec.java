package swift.antidote.conv.codecs.primitives;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;
import swift.antidote.conv.Codec;

public class StringCodec implements Codec<String> {
    @Override
    public String decode(OtpErlangObject object) {
        OtpErlangString cast = (OtpErlangString) object;
        return cast.stringValue();
    }

    @Override
    public OtpErlangObject encode(String object) {
        return new OtpErlangString(object);
    }
}
