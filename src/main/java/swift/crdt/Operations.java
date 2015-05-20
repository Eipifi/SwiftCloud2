package swift.crdt;

import com.google.common.reflect.Reflection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;

public final class Operations {

    private Operations() { /* no instances allowed */ }

    /**
     * Calls the specified method with specified arguments
     * @param target CRDT object to be called
     * @param method operation name
     * @param args operation arguments
     */
    public static void call(Object target, String method, Object... args) {
        Method found = null;
        for (Method m: target.getClass().getMethods()) {
            Operation op = m.getAnnotation(Operation.class);
            if (op != null && op.name().equals(method)) {
                found = m;
                break;
            }
        }
        if (found == null) throw new IllegalArgumentException("Method not found");
        try {
            found.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Failed to call method", e);
        }
    }

    /**
     * Creates a proxy object that tracks operation invocations
     * @param object CRDT object to be tracked
     * @param ifce CRDT interface
     * @param sink call listener
     * @param <T> interface type
     * @return proxy object
     */
    @SuppressWarnings("unchecked")
    public static <T> T proxy(T object, Class<T> ifce, BiConsumer<String, Object[]> sink) {
        Object proxyObject = Reflection.newProxy(ifce, (proxy, method, args) -> {
            Operation annotation = method.getAnnotation(Operation.class);
            if (annotation != null) sink.accept(annotation.name(), args);
            return method.invoke(object, args);
        });
        return (T) proxyObject;
    }

}
