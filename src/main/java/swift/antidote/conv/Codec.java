package swift.antidote.conv;

import com.ericsson.otp.erlang.OtpErlangObject;

public interface Codec<T> {
    T decode(OtpErlangObject object);
    OtpErlangObject encode(T object);

    default T tryDecode(OtpErlangObject object) {
        try {
            return decode(object);
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

    default OtpErlangObject tryEncode(T object) {
        try {
            return encode(object);
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }
}
