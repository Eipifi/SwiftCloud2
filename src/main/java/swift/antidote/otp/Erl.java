package swift.antidote.otp;

import com.ericsson.otp.erlang.*;
import com.google.common.collect.Lists;

import java.util.List;

public class Erl {

    @SuppressWarnings("unchecked")
    public static <T> T cast(OtpErlangObject object) {
        try
        {
            return (T) object;
        }
        catch (ClassCastException e) {
            throw new MatchException(e);
        }
    }

    public static OtpErlangList list(OtpErlangObject object) {
        return cast(object);
    }

    public static OtpErlangTuple tuple(OtpErlangObject object) {
        return cast(object);
    }

    public static OtpErlangAtom atom(OtpErlangObject object) {
        return cast(object);
    }

    public static List<OtpErlangObject> toList(OtpErlangObject object) {
        return Lists.newArrayList(list(object));
    }

    public static OtpErlangObject response(OtpErlangObject object) {
        OtpErlangTuple tuple = tuple(object);
        if (! new OtpErlangAtom("ok").equals(tuple.elementAt(0))) {
            throw new MatchException("First field of response is not atom[ok]");
        }
        if (tuple.arity() != 2) {
            throw new MatchException("Expected response arity: 2");
        }
        return tuple.elementAt(1);
    }

    public static OtpErlangTuple makeTuple(OtpErlangObject... objects) {
        return new OtpErlangTuple(objects);
    }

    public static OtpErlangList makeList(List<? extends OtpErlangObject> objects) {
        return new OtpErlangList(objects.toArray(new OtpErlangObject[objects.size()]));
    }

    public static OtpErlangAtom makeAtom(String contents) {
        return new OtpErlangAtom(contents);
    }

    public static String toString(OtpErlangObject object) {
        OtpErlangString s = cast(object);
        return s.stringValue();
    }

    public static long toLong(OtpErlangObject object) {
        OtpErlangLong l = cast(object);
        return l.longValue();
    }

    public static class MatchException extends IllegalArgumentException {
        public MatchException(Exception e) {
            super(e);
        }
        public MatchException(String cause) {
            super(cause);
        }
    }
}
