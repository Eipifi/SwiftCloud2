package swift.antidote.otp.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.conv.Codec;
import swift.antidote.otp.conv.Erl2;
import swift.core.Clock;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClockCodec implements Codec<Clock> {
    @Override
    public Clock decode(OtpErlangObject object) {
        OtpErlangList cast = (OtpErlangList) object;
        return StreamSupport.stream(cast.spliterator(), false)
                .map(this::decodeEntry)
                .map(Clock::create)
                .reduce(Clock::merge)
                .orElse(Clock.EMPTY);
    }

    @Override
    public OtpErlangObject encode(Clock clock) {
        return Erl2.list(StreamSupport.stream(clock.spliterator(), false).map(Erl2::encode).collect(Collectors.toList()));
    }

    private Clock.Entry decodeEntry(OtpErlangObject object) {
        return Erl2.decode(object, Clock.Entry.class);
    }
}
