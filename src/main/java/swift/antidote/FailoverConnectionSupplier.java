package swift.antidote;

import com.ericsson.otp.erlang.OtpAuthException;
import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpPeer;
import com.ericsson.otp.erlang.OtpSelf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Supplier;

public class FailoverConnectionSupplier implements Supplier<OtpConnection> {

    private static final Logger log = LoggerFactory.getLogger(FailoverConnectionSupplier.class);
    private static final int RETRIES = 3;

    private final OtpSelf otpSelf;
    private final String[] peers;
    private OtpConnection connection;
    private int current = 0;

    public FailoverConnectionSupplier(String client, String cookie, String... peers) throws IOException {
        otpSelf = new OtpSelf(client, cookie);
        this.peers = peers;
    }

    @Override
    public synchronized OtpConnection get() {
        for (int i = 0; i < RETRIES; ++i) {
            if (connection != null && connection.isConnected()) return connection;
            connection = tryEstablishConnection();
            if (connection != null) return connection;
        }
        return null;
    }

    private OtpConnection tryEstablishConnection() {
        int ptr = current;
        current = (current + 1) % peers.length;
        try {
            return otpSelf.connect(new OtpPeer(peers[ptr]));
        } catch (OtpAuthException | IOException e) {
            log.warn("Failed to connect to Antidote DC {} (Reason: {})", peers[ptr], e);
            return null;
        }
    }

}
