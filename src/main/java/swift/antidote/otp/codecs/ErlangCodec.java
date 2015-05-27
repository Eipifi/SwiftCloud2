package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;

public interface ErlangCodec<T> {
    T decode(OtpErlangObject object);
    OtpErlangObject encode(T object);
    OtpErlangObject erlangType();
}
