package swift.antidote.otp.conv;

public class ConversionException extends RuntimeException {

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(Exception e) {
        super(e);
    }
}
