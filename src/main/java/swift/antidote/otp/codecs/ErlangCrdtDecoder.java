package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.core.OperationLog;

public interface ErlangCrdtDecoder<T> {
    OtpErlangObject erlangType();
    OtpErlangObject encodeOperation(OperationLog operation);
    T decode(OtpErlangObject object);
}
