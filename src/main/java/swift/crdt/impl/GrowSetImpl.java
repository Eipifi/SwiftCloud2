package swift.crdt.impl;

import com.google.common.collect.Iterators;
import swift.crdt.Operation;
import swift.crdt.types.GrowSet;
import swift.utils.AlmostSet;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class GrowSetImpl<T> implements GrowSet<T>, AlmostSet<T> {

    private final Set<T> set;

    public GrowSetImpl() {
        this.set = new HashSet<>();
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.unmodifiableIterator(set.iterator());
    }

    @Override
    @Operation(name = "add")
    public boolean add(T element) {
        return set.add(element);
    }

    @Override
    public boolean remove(Object o) {
        throw new IllegalStateException("Cannot remove elements from a grow-only set");
    }
}
