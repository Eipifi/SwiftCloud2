package example.simple;

import com.ericsson.otp.erlang.OtpConnection;
import swift.antidote.otp.AntidoteOtpScoutAdapter;
import swift.antidote.otp.FailoverConnectionSupplier;
import swift.core.*;
import swift.storage.FileScoutIdentity;

import java.io.IOException;
import java.util.function.Supplier;

public final class ScoutBuilder {

    public static Scout tryBuild() throws IOException, InterruptedException {
        ScoutIdentity scoutIdentity = new FileScoutIdentity("identity.properties");
        Supplier<OtpConnection> sup = new FailoverConnectionSupplier("sc@127.0.0.1", "antidote", "antidote@127.0.0.1");
        ScoutAdapter adapter = new AntidoteOtpScoutAdapter(sup, scoutIdentity.scoutName());
        Scout scout = new GeneralScout(adapter, scoutIdentity::getNextTxnId);
        while(scout.clock().eq(Clock.EMPTY)) {
            Thread.sleep(100);
        }
        return scout;
    }

    public static Scout build() {
        try {
            return tryBuild();
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
