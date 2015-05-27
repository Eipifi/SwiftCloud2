package swift.antidote.otp.codecs;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.Erl;
import swift.core.Clock;
import swift.core.Transaction;

import java.util.stream.Collectors;

public class TransactionCodec implements ErlangCodec<Transaction> {
    @Override
    public Transaction decode(OtpErlangObject object) {
        // Not needed
        throw new IllegalStateException("Not applicable");
    }

    @Override
    public OtpErlangObject encode(Transaction txn) {
        return Erl.makeTuple(
                Codecs.getCodec(Clock.class).encode(txn.getDependencies()),
                Erl.makeList(txn.getOperations().stream().map(Codecs::encode).collect(Collectors.toList()))
        );
    }
}
