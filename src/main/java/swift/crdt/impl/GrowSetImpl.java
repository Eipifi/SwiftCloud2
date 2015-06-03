package swift.crdt.impl;

import com.google.common.collect.Iterators;
import swift.crdt.Operation;
import swift.crdt.types.GrowSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GrowSetImpl implements GrowSet {

    private Set<Object> underlyingSet = new HashSet<>();

    public GrowSetImpl(Collection<Object> elements) {
        underlyingSet.addAll(elements);
    }

    @Override
    public boolean contains(Object obj) {
        return underlyingSet.contains(obj);
    }

    @Override
    @Operation(name = "add")
    public void add(Object obj) {
        underlyingSet.add(obj);
    }

    @Override
    public Iterator<Object> iterator() {
        return Iterators.unmodifiableIterator(underlyingSet.iterator());
    }
}
