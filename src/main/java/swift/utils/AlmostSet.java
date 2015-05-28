package swift.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public interface AlmostSet<T> extends Set<T> {

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default Object[] toArray() {
        return Iterables.toArray(this, Object.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    default <T1> T1[] toArray(T1[] a) {
        // Curse you, Java
        Object[] b = toArray();
        T1[] r = a.length >= b.length ? a : (T1[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), b.length);
        for (int i = 0; i < b.length; ++i) r[i] = (T1) b[i];
        return r;
    }

    @Override
    default boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    default boolean addAll(Collection<? extends T> c) {
        return c.stream().map(this::add).reduce((a, b) -> a || b).orElse(false);
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        return removeAll(Sets.difference(this, Sets.newHashSet(c)));
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        return c.stream().map(this::remove).reduce((a, b) -> a || b).orElse(false);
    }

    @Override
    default void clear() {
        retainAll(Collections.emptySet());
    }
}
