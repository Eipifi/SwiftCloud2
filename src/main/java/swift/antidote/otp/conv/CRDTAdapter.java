package swift.antidote.otp.conv;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangObject;

public interface CRDTAdapter<T> {
    T decode(OtpErlangObject object);
    OtpErlangAtom type();
    OtpErlangObject encodeOp(String method, Object[] args);
}
