package swift.antidote.otp;

import com.ericsson.otp.erlang.OtpConnection;
import com.google.common.util.concurrent.Uninterruptibles;
import swift.core.Clock;
import swift.core.GeneralScout;
import swift.core.Scout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Antidote {

    public static Scout getDefaultScout() {
        try {
            Supplier<OtpConnection> sup = new FailoverConnectionSupplier("sc@127.0.0.1", "antidote", "antidote@127.0.0.1");
            GeneralScout scout = new GeneralScout(new AntidoteOtpScoutAdapter(sup));

            while(scout.clock().eq(Clock.EMPTY)) {
                Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
            }

            return scout;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
