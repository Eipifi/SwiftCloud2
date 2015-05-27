package swift.antidote.otp;

import com.ericsson.otp.erlang.OtpConnection;
import swift.core.GeneralScout;
import swift.core.Scout;

import java.io.IOException;
import java.util.function.Supplier;

public class Antidote {

    public static Scout getDefaultScout() {
        try {
            Supplier<OtpConnection> sup = new FailoverConnectionSupplier("sc@127.0.0.1", "antidote", "antidote@127.0.0.1");
            return new GeneralScout(new AntidoteOtpScoutAdapter(sup));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
