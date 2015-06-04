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

            GrowSet gset = txn.read("example_set", GrowSet.class);
            gset.add(System.currentTimeMillis());
            gset.forEach(System.out::println);

            txn.commit();
        }
        finally
        {
            scout.close();
        }
    }
}
