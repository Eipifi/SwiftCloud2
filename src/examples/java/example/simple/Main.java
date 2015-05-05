package example.simple;

import com.basho.riak.client.api.RiakClient;
import swift.antidote.api.GetCounter;
import swift.antidote.api.GetServerInfo;
import swift.antidote.api.IncrementCounter;
import swift.antidote.operations.GetServerInfoOperation;
import swift.antidote.utils.OID;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class Main {

    public static final InetSocketAddress[] ADDRESSES = {new InetSocketAddress("172.16.0.159", 10017)};

    public static void main(String[] args) throws UnknownHostException, ExecutionException, InterruptedException {
        System.out.println("Running");
        RiakClient riakClient = RiakClient.newClient(ADDRESSES);

        GetServerInfoOperation.Response rsp = riakClient.execute(new GetServerInfo());
        System.out.println("Node: " + rsp.node);
        System.out.println("Version: " + rsp.version);

        OID oid = new OID("example_key");

        Integer rsp2 = riakClient.execute(new GetCounter(oid));
        System.out.println("Counter val: " + rsp2);

        riakClient.execute(new IncrementCounter(oid, 5));

        rsp2 = riakClient.execute(new GetCounter(oid));
        System.out.println("Counter val: " + rsp2);

        riakClient.shutdown();
    }
}
