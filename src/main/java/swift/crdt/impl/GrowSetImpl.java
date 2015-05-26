package swift.crdt.impl;

import swift.crdt.types.GrowSet;
import java.util.Iterator;

public class GrowSetImpl<T> implements GrowSet<T> {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        for(T e: this) {

        }
        return null;
    }
}
