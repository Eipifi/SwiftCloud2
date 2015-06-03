package swift.antidote.otp.conv;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import org.javatuples.Pair;
import swift.antidote.otp.conv.codecs.crdts.GrowCounterAdapter;
import swift.antidote.otp.conv.codecs.primitives.LongCodec;
import swift.antidote.otp.conv.codecs.primitives.StringCodec;
import swift.antidote.otp.conv.codecs.swift.*;
import swift.core.Clock;
import swift.core.OID;
import swift.core.OperationLog;
import swift.core.Transaction;
import swift.crdt.types.GrowCounter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Erl2 {

    private static final Map<Class, Codec> codecs = new HashMap<>();
    private static final Map<Class, CRDTAdapter> adapters = new HashMap<>();

    static {
        codecs.put(Long.class, new LongCodec());
        codecs.put(String.class, new StringCodec());

        codecs.put(OID.class, new OIDCodec());
        codecs.put(Clock.class, new ClockCodec());
        codecs.put(Clock.Entry.class, new ClockEntryCodec());
        codecs.put(Transaction.class, new TransactionCodec());
        codecs.put(OperationLog.class, new OperationLogCodec());

        adapters.put(GrowCounter.class, new GrowCounterAdapter());
    }

    public static OtpErlangObject encode(Object object) {
        return encode(object, object.getClass());
    }

    @SuppressWarnings("unchecked")
    public static OtpErlangObject encode(Object object, Class typeHint) {
        return codec(typeHint).tryEncode(object);
    }

    public static <T> T decode(OtpErlangObject object, Class<? super T> typeHint) {
        return codec(typeHint).tryDecode(object);
    }

    public static OtpErlangList encodeAll(Collection<?> objects) {
        return list(StreamSupport.stream(objects.spliterator(), false).map(Erl2::encode).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    private static <T> Codec<T> codec(Class<? super T> type) {
        Codec codec = codecs.get(type);
        if (codec == null) throw new ConversionException("Unknown codec for type " + type);
        return codec;
    }

    @SuppressWarnings("unchecked")
    public static <T> CRDTAdapter<T> adapter(Class<T> type) {
        return adapters.get(type);
    }

    //////////////////////////////////////////////////////////////////////

    public static OtpErlangList list(Collection<OtpErlangObject> objects) {
        return new OtpErlangList(objects.toArray(new OtpErlangObject[objects.size()]));
    }

    public static OtpErlangTuple tuple(OtpErlangObject... objects) {
        return new OtpErlangTuple(objects);
    }

    public static OtpErlangObject response(OtpErlangObject object) {
        OtpErlangTuple tuple = tuple(object);
        if (! new OtpErlangAtom("ok").equals(tuple.elementAt(0))) {
            throw new ConversionException("First field of response is not atom[ok]");
        }
        if (tuple.arity() != 2) {
            throw new ConversionException("Expected response arity: 2");
        }
        return tuple.elementAt(1);
    }

}
