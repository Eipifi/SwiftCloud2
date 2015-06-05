package example.simple;

import swift.core.Scout;
import swift.core.Transaction;
import swift.crdt.types.GrowCounter;
import swift.crdt.types.GrowSet;

public class Main {

    public static void main(String[] args) throws Exception {
        Scout scout = ScoutBuilder.build();
        try
        {
            Transaction txn = scout.newTransaction();

            GrowCounter counter = txn.read("example_counter", GrowCounter.class);
            counter.increment(3);
            System.out.println(counter.value());

            txn.commit();
        }
        finally
        {
            scout.close();
        }
    }
}
