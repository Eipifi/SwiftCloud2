package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.*;
import swift.antidote.otp.Erl;
import swift.core.Clock;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClockCodec implements ErlangCodec<Clock> {

    @Override
    public Clock decode(OtpErlangObject object) {
        return Erl.toList(object).stream().map(this::getEntry).reduce(Clock::merge).orElse(Clock.EMPTY);
    }

    @Override
    public OtpErlangObject encode(Clock object) {
        List<OtpErlangTuple> list = StreamSupport.stream(object.spliterator(), false)
                .map((e) -> new OtpErlangTuple(new OtpErlangObject[]{clockKeyToObj(e.key), new OtpErlangLong(e.value)}))
                .collect(Collectors.toList());
        return new OtpErlangList(list.toArray(new OtpErlangObject[list.size()]));
    }

    @Override
    public OtpErlangObject erlangType() {
        throw new IllegalStateException("Not applicable");
    }

    private Clock getEntry(OtpErlangObject object) {
        OtpErlangTuple tuple = Erl.tuple(object);
        return Clock.create(getClockKey(tuple.elementAt(0)), Erl.toLong(tuple.elementAt(1)));
    }

    private String getClockKey(OtpErlangObject object) {
        OtpErlangTuple tuple = Erl.cast(object);
        OtpErlangAtom dcName = Erl.atom(tuple.elementAt(0));
        OtpErlangTuple numbers = Erl.tuple(tuple.elementAt(1));
        return dcName.atomValue() + "/" + numbers.elementAt(0) + "/" + numbers.elementAt(1) + "/" + numbers.elementAt(2);
    }

    private OtpErlangObject clockKeyToObj(String key) {
        String[] parts = key.split("/");
        return new OtpErlangTuple(new OtpErlangObject[]{
                new OtpErlangAtom(parts[0]),
                new OtpErlangTuple(new OtpErlangObject[]{
                        new OtpErlangInt(Integer.parseInt(parts[1])),
                        new OtpErlangInt(Integer.parseInt(parts[2])),
                        new OtpErlangInt(Integer.parseInt(parts[3]))
                })
        });
    }
}
