package swift.core;

import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;

public class Clock {

    private final Map<String, Long> values = new HashMap<>();

    public long getEntry(String key) {
        return values.containsKey(key) ? values.get(key) : 0;
    }

    public boolean greaterThan(Clock that) {
        for(String s: Sets.union(this.values.keySet(), that.values.keySet())) {
            if (this.getEntry(s) < that.getEntry(s)) {
                return false;
            }
        }
        return true;
    }

    public static Clock max(Clock a, Clock b) {
        Clock c = new Clock();
        for(String s: Sets.union(a.values.keySet(), b.values.keySet())) {
            c.values.put(s, Math.max(a.getEntry(s), b.getEntry(s)));
        }
        return c;
    }
}
