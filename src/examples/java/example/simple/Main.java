package example.simple;

import com.basho.riak.client.api.RiakClient;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class Main {

    public static final InetSocketAddress[] ADDRESSES = {new InetSocketAddress("172.16.0.159", 10017)};

    public static void main(String[] args) throws UnknownHostException {
        System.out.println("Running");
        RiakClient riakClient = RiakClient.newClient(ADDRESSES);

        riakClient.shutdown();
    }
}
