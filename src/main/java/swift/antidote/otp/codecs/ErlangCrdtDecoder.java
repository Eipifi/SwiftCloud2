package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;

public interface ErlangCrdtDecoder<T> {
    OtpErlangObject erlangType();
    T decode(OtpErlangObject object);
}
