package swift.core;

import java.util.Arrays;

public final class OperationLog {

    private final OID oid;
    private final String method;
    private final Object[] args;

    public OperationLog(OID oid, String method, Object[] args) {
        this.oid = oid;
        this.method = method;
        this.args = args;
    }

    public OID getOid() {
        return oid;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return String.format("oid=\"%s\" call=%s(%s)", getOid(), getMethod(), Arrays.toString(getArgs()));
    }
}
