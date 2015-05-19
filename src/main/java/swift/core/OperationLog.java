package swift.core;

public class OperationLog {

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
}
