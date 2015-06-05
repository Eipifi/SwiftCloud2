package swift.antidote.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.conv.Codec;
import swift.antidote.conv.Erl;
import swift.core.Transaction;

public class TransactionCodec implements Codec<Transaction> {
    @Override
    public Transaction decode(OtpErlangObject object) {
        throw new IllegalStateException("Not implemented / required");
    }

    @Override
    public OtpErlangObject encode(Transaction object) {
        return Erl.tuple(
                Erl.encode(object.getDependencies()),
                Erl.encodeAll(object.getOperations())
        );
    }
}
