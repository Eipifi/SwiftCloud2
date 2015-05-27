package swift.antidote.otp.codecs;

import swift.core.Clock;
import swift.core.OID;
import swift.crdt.types.GrowCounter;

import java.util.HashMap;
import java.util.Map;

public class Codecs {

    private static final Map<Class, ErlangCodec> codecs = new HashMap<>();

    static {
        codecs.put(Clock.class, new ClockCodec());
        codecs.put(OID.class, new OIDCodec());
        codecs.put(GrowCounter.class, new GrowCounterCodec());
    }

    @SuppressWarnings("unchecked")
    public static <T> ErlangCodec<T> getCodec(Class<T> type) {
        return codecs.get(type);
    }

}
