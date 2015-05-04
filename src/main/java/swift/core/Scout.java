package swift.core;

public interface Scout {
    Transaction newTransaction();
    void stop(boolean waitForCommit);
    String Id();
}
