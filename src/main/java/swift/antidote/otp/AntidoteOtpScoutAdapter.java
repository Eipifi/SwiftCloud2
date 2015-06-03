package swift.antidote.otp;

import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swift.antidote.otp.codecs.Codecs;
import swift.antidote.otp.conv.Erl2;
import swift.core.Clock;
import swift.core.OID;
import swift.core.ScoutAdapter;
import swift.core.Transaction;

import java.util.function.Supplier;

public class AntidoteOtpScoutAdapter implements ScoutAdapter {

    private static final long K_DURABILITY = 2;

    private static final String SC_MODULE = "swiftcloud_api";
    private static final Logger log = LoggerFactory.getLogger(AntidoteOtpScoutAdapter.class);

    private final Supplier<OtpConnection> connectionSupplier;
    private final String scoutId;

    public AntidoteOtpScoutAdapter(Supplier<OtpConnection> connectionSupplier, String scoudId) {
        this.connectionSupplier = connectionSupplier;
        this.scoutId = scoudId;
    }

    private synchronized OtpErlangObject rpc(String method, OtpErlangObject... args) throws Exception {
        log.info("RPC: sending method={} args={}", method, args);
        OtpConnection conn = connectionSupplier.get();
        if (conn == null) throw new IllegalStateException("Connection unavailable");
        conn.sendRPC(SC_MODULE, method, args);
        OtpErlangObject response = conn.receiveRPC();
        log.info("RPC: received {}", response);
        return Erl.response(response);
    }

    @Override
    public Clock tryGetClock() {
        try {
            return Erl2.decode(rpc("get_clock", Codecs.encode(K_DURABILITY)), Clock.class);
        } catch (Exception e) {
            log.warn("Failed to retrieve clock", e);
            return null;
        }
    }

    @Override
    public Clock.Entry tryCommit(Transaction transaction, long id) {
        try {
            return Erl2.decode(rpc("execute_transaction", Erl2.tuple(Erl2.encode(scoutId), Erl2.encode(id)), Erl2.encode(transaction, Transaction.class)), Clock.Entry.class);
        } catch (Exception e) {
            log.warn("Failed to retrieve clock", e);
            return null;
        }
    }

    @Override
    public ObjectAndClock tryRead(OID oid, Class type, Clock dependencies) {
        try {
            OtpErlangTuple response = (OtpErlangTuple) rpc("read_object", Erl2.encode(dependencies), Erl2.encode(oid), Erl2.adapter(type).type());
            ObjectAndClock result = new ObjectAndClock();
            result.object = Erl2.adapter(type).decode(response.elementAt(0));
            result.clock = Erl2.decode(response.elementAt(1), Clock.class);
            return result;
        } catch (Exception e) {
            log.warn("Failed tor retrieve object", e);
            return null;
        }
    }
}
