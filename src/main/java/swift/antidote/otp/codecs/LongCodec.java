package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.Erl;

public class LongCodec implements ErlangCodec<Long> {

    @Override
    public Long decode(OtpErlangObject object) {
        return Erl.toLong(object);
    }

    @Override
    public OtpErlangObject encode(Long object) {
        return new OtpErlangLong(object);
    }
}
