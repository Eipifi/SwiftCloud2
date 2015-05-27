package swift.antidote.otp;

import com.ericsson.otp.erlang.OtpConnection;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swift.antidote.otp.codecs.Codecs;
import swift.core.Clock;
import swift.core.OID;
import swift.core.ScoutAdapter;
import swift.core.Transaction;
import java.util.function.Supplier;

public class AntidoteOtpScoutAdapter implements ScoutAdapter {

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
            return Codecs.getCodec(Clock.class).decode(rpc("get_clock"));
        } catch (Exception e) {
            log.warn("Failed to retrieve clock", e);
            return null;
        }
    }

    @Override
    public Clock.Entry tryCommit(Transaction transaction, long id) {
        try {
            OtpErlangObject encoded_txn = Codecs.getCodec(Transaction.class).encode(transaction);
            OtpErlangObject otid = Erl.makeTuple(Erl.makeString(scoutId), Codecs.encode(id));
            OtpErlangObject response = rpc("execute_transaction", otid, encoded_txn);
            return Codecs.getCodec(Clock.Entry.class).decode(response);
        } catch (Exception e) {
            log.warn("Failed to retrieve clock", e);
            return null;
        }
    }

    @Override
    public ObjectAndClock tryRead(OID oid, Class type, Clock dependencies) {
        try {
            OtpErlangObject response = rpc("read_object",
                    Codecs.encode(dependencies),
                    Codecs.encode(oid),
                    Codecs.getDecoder(type).erlangType());
            OtpErlangTuple tuple = Erl.tuple(response);
            ObjectAndClock result = new ObjectAndClock();
            result.object = Codecs.getDecoder(type).decode(tuple.elementAt(0));
            result.clock = Codecs.getCodec(Clock.class).decode(tuple.elementAt(1));
            return result;
        } catch (Exception e) {
            log.warn("Failed tor retrieve object", e);
            return null;
        }
    }
}
