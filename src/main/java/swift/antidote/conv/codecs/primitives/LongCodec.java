package swift.antidote.conv.codecs.primitives;

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.conv.Codec;

public class LongCodec implements Codec<Long> {
    @Override
    public Long decode(OtpErlangObject object) {
        OtpErlangLong cast = (OtpErlangLong) object;
        return cast.longValue();
    }

    @Override
    public OtpErlangObject encode(Long object) {
        return new OtpErlangLong(object);
    }
}
