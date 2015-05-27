package swift.core;

import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;

public final class Clock implements Iterable<Clock.Entry> {

    public static final Clock EMPTY = new Clock();

    private final Map<String, Long> data = new HashMap<>();

    public long get(String key) {
        Long value = data.get(key);
        return value == null ? 0 : value;
    }

    public Clock merge(Clock that) {
        Clock result = new Clock();
        for(String key: Sets.union(this.data.keySet(), that.data.keySet())) {
            result.data.put(key, Math.max(this.get(key), that.get(key)));
        }
        return result;
    }

    public Clock with(String key, long value) {
        return merge(create(key, value));
    }

    public Clock without(String key) {
        Clock result = new Clock();
        data.forEach((k, v) -> {
            if (!Objects.equals(k, key)) result.data.put(k, v);
        });
        return result;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public static Clock create(String key, long value) {
        Clock tmp = new Clock();
        tmp.data.put(key, value);
        return tmp;
    }

    public static Clock create(Entry entry) {
        return create(entry.key, entry.value);
    }

    /**
     * Equals
     * @param that
     * @return
     */
    public boolean eq(Clock that) {
        for(String s: Sets.union(this.data.keySet(), that.data.keySet())) {
            if (this.get(s) != that.get(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Greater than or equal
     * @param that
     * @return
     */
    public boolean ge(Clock that) {
        for(String s: Sets.union(this.data.keySet(), that.data.keySet())) {
            if (this.get(s) < that.get(s)) {
                return false;
            }
        }
        return true;
    }

    public Set<Entry> entries() {
        return data.entrySet().stream().map((e) -> new Entry(e.getKey(), e.getValue())).collect(Collectors.toSet());
    }

    @Override
    public Iterator<Entry> iterator() {
        return entries().iterator();
    }

    public static class Entry {
        public final String key;
        public final long value;

        public Entry(String key, long value) {
            this.key = key;
            this.value = value;
        }
    }
}
