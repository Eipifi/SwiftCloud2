package swift.antidote.otp.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.otp.conv.Codec;
import swift.antidote.otp.conv.Erl;
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
