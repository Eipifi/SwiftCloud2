package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.core.Clock;
import swift.core.OID;
import swift.core.OperationLog;
import swift.core.Transaction;
import swift.crdt.types.GrowCounter;

import java.util.HashMap;
import java.util.Map;

public class Codecs {

    private static final Map<Class, ErlangCrdtDecoder> decoders = new HashMap<>();
    private static final Map<Class, ErlangCodec> codecs = new HashMap<>();

    static {
        codecs.put(Clock.class, new ClockCodec());
        codecs.put(Clock.Entry.class, new ClockEntryCodec());
        codecs.put(OID.class, new OIDCodec());
        codecs.put(Long.class, new LongCodec());
        codecs.put(Transaction.class, new TransactionCodec());
        codecs.put(OperationLog.class, new OperationLogCodec());
        decoders.put(GrowCounter.class, new GrowCounterCodec());
    }

    @SuppressWarnings("unchecked")
    public static <T> ErlangCodec<T> getCodec(Class<T> type) {
        return codecs.get(type);
    }

    @SuppressWarnings("unchecked")
    public static OtpErlangObject encode(Object object) {
        ErlangCodec codec = getCodec(object.getClass());
        return codec.encode(object);
    }

    @SuppressWarnings("unchecked")
    public static <T> ErlangCrdtDecoder<T> getDecoder(Class<T> type) {
        return decoders.get(type);
    }

}
