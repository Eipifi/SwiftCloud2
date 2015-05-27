package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import swift.antidote.otp.Erl;
import swift.core.Clock;

public class ClockEntryCodec implements ErlangCodec<Clock.Entry> {
    @Override
    public Clock.Entry decode(OtpErlangObject object) {
        OtpErlangTuple tuple = Erl.tuple(object);
        return new Clock.Entry(getClockKey(tuple.elementAt(0)), Erl.toLong(tuple.elementAt(1)));
    }

    @Override
    public OtpErlangObject encode(Clock.Entry object) {
        return Erl.makeTuple(
                clockKeyToObj(object.key),
                Codecs.encode(object.value)
        );
    }

    private String getClockKey(OtpErlangObject object) {
        OtpErlangTuple tuple = Erl.cast(object);
        OtpErlangAtom dcName = Erl.atom(tuple.elementAt(0));
        OtpErlangTuple numbers = Erl.tuple(tuple.elementAt(1));
        return dcName.atomValue() + "/" + numbers.elementAt(0) + "/" + numbers.elementAt(1) + "/" + numbers.elementAt(2);
    }

    private OtpErlangTuple clockKeyToObj(String key) {
        String[] parts = key.split("/");
        return Erl.makeTuple(
                new OtpErlangAtom(parts[0]),
                Erl.makeTuple(
                        new OtpErlangInt(Integer.parseInt(parts[1])),
                        new OtpErlangInt(Integer.parseInt(parts[2])),
                        new OtpErlangInt(Integer.parseInt(parts[3]))
                )
        );
    }
}
