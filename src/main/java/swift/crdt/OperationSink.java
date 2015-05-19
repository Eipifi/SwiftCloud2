package swift.crdt;

@FunctionalInterface
public interface OperationSink {
    void operationCalled(String method, Object[] args);
}
