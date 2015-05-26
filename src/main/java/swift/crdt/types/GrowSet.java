package swift.crdt.types;

import java.util.Collection;
import java.util.Set;

public interface GrowSet<T> extends Set<T> {

    default boolean isEmpty() {
        return size() == 0;
    }

    default boolean remove(Object o) {
        throw new IllegalStateException("Can not remove elements from a grow-only set");
    }

    default boolean containsAll(Collection<?> c) {
        for(Object e: c) if (!contains(e)) return false;
        return true;
    }

    default boolean addAll(Collection<? extends T> c) {
        boolean result = false;
        for(T e: c) result |= add(e);
        return result;
    }

    default boolean retainAll(Collection<?> c) {
        throw new IllegalStateException("Can not remove elements from a grow-only set");
    }

    default boolean removeAll(Collection<?> c) {
        throw new IllegalStateException("Can not remove elements from a grow-only set");
    }

    default void clear() {
        throw new IllegalStateException("Can not remove elements from a grow-only set");
    }


}
