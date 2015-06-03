package swift.antidote.otp.conv.codecs.swift;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import swift.antidote.otp.conv.Erl2;
import swift.antidote.otp.conv.Codec;

public class DCIDCodec implements Codec<String> {

    @Override
    public String decode(OtpErlangObject object) {
        OtpErlangTuple tuple = (OtpErlangTuple) object;
        OtpErlangAtom dcName = (OtpErlangAtom) tuple.elementAt(0);
        OtpErlangTuple numbers = (OtpErlangTuple) tuple.elementAt(1);
        return dcName.atomValue() + "/" + numbers.elementAt(0) + "/" + numbers.elementAt(1) + "/" + numbers.elementAt(2);
    }

    @Override
    public OtpErlangObject encode(String object) {
        String[] parts = object.split("/");
        return Erl2.tuple(
                new OtpErlangAtom(parts[0]),
                Erl2.tuple(
                        new OtpErlangInt(Integer.parseInt(parts[1])),
                        new OtpErlangInt(Integer.parseInt(parts[2])),
                        new OtpErlangInt(Integer.parseInt(parts[3]))
                )
        );
    }
}
