package example.simple;

import swift.antidote.DirectAntidoteScout;
import swift.core.Scout;
import swift.core.Transaction;
import swift.crdt.types.GrowCounter;

public class Main {

    public static void main(String[] args) {

        Scout scout = DirectAntidoteScout.connectDefault();
        Transaction txn = scout.newTransaction();

        GrowCounter counter = txn.read("example_counter", GrowCounter.class);
        counter.increment();
        System.out.println("Counter value: " + counter.value());

        txn.commit();
        scout.close();
    }
}
