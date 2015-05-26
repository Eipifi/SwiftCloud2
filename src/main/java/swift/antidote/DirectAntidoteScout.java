package swift.antidote;

import com.basho.riak.client.api.RiakClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swift.core.*;
import swift.crdt.impl.GrowCounterImpl;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class DirectAntidoteScout implements ScoutAdapter {

    private static Logger log = LoggerFactory.getLogger(DirectAntidoteScout.class);
    private RiakClient rc;

    private DirectAntidoteScout() { /* Instances are created through connect() */ }

    @Override
    public Clock tryGetClock() {
        return null;
    }

    @Override
    public Clock tryCommit(Transaction transaction, long id) {
        return null;
    }

    @Override
    public Object tryRead(OID oid, Class type, Clock dependencies) {
        return new GrowCounterImpl();
    }

    /**
     * Creates the new scout instance.
     * The method blocks until the connection is established (in order to obtain the dependency clock).
     * To avoid start blocking, a durable storage could be implemented to hold the last observed
     * k-durable clock as well as the object cache contents.
     * @param addresses list of Antidote addresses
     * @return scout
     */
    public static Scout connect(InetSocketAddress... addresses) {
        try {
            DirectAntidoteScout adapter = new DirectAntidoteScout();
            adapter.rc = RiakClient.newClient(addresses);
            log.info("Connected to Antidote DC");
            return new GeneralScout(adapter);
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Failed to connect to Antidote server", e);
        }
    }

    /**
     * Usability function - creates a new scout and connects it to the default address.
     * @return scout
     */
    public static Scout connectDefault() {
        return connect(new InetSocketAddress("localhost", 10017));
    }
}
