package swift.antidote.otp.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import swift.antidote.otp.conv.Codec;
import swift.antidote.otp.conv.Erl2;
import swift.core.Clock;

public class ClockEntryCodec implements Codec<Clock.Entry> {

    private Codec<String> dcidCodec = new DCIDCodec();

    @Override
    public Clock.Entry decode(OtpErlangObject object) {
        OtpErlangTuple tuple = (OtpErlangTuple) object;
        return new Clock.Entry(
                dcidCodec.tryDecode(tuple.elementAt(0)),
                Erl2.decode(tuple.elementAt(1), Long.class)
        );
    }

    @Override
    public OtpErlangObject encode(Clock.Entry object) {
        return Erl2.tuple(
                dcidCodec.encode(object.key),
                Erl2.encode(object.value)
        );
    }
}
