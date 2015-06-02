package example.simple;

import swift.core.Scout;
import swift.core.Transaction;
import swift.crdt.types.GrowCounter;

public class Main {

    public static void main(String[] args) throws Exception {
        Scout scout = ScoutBuilder.build();
        try
        {
            Transaction txn = scout.newTransaction();

            GrowCounter counter = txn.read("example_counter", GrowCounter.class);
            counter.increment();
            System.out.println("Counter value: " + counter.value());

            txn.commit();
        }
        finally
        {
            scout.close();
        }
    }
}
