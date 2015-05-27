package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.*;
import swift.antidote.otp.Erl;
import swift.core.Clock;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClockCodec implements ErlangCodec<Clock> {

    @Override
    public Clock decode(OtpErlangObject object) {
        return Erl.toList(object).stream()
                .map((o) -> Codecs.getCodec(Clock.Entry.class).decode(o))
                .map(Clock::create)
                .reduce(Clock::merge)
                .orElse(Clock.EMPTY);
    }

    @Override
    public OtpErlangList encode(Clock object) {
        return Erl.makeList(StreamSupport.stream(object.spliterator(), false)
                .map(Codecs::encode)
                .collect(Collectors.toList()));
    }
}
