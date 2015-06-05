package swift.antidote.conv.codecs.crdts;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import swift.antidote.conv.CRDTAdapter;
import swift.antidote.conv.Erl;
import swift.crdt.impl.GrowSetImpl;
import swift.crdt.types.GrowSet;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GrowSetAdapter implements CRDTAdapter<GrowSet> {
    @Override
    public GrowSet decode(OtpErlangObject object) {
        OtpErlangList list = (OtpErlangList) object;
        return new GrowSetImpl(StreamSupport.stream(list.spliterator(), false).map(Erl::decodePrimitive).collect(Collectors.toList()));
    }

    @Override
    public OtpErlangAtom type() {
        return new OtpErlangAtom("riak_dt_gset");
    }

    @Override
    public OtpErlangObject encodeOp(String method, Object[] args) {
        return Erl.tuple(
                new OtpErlangAtom("add"),
                Erl.encode(args[0])
        );
    }
}
