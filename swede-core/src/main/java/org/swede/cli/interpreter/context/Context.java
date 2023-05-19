package org.swede.cli.interpreter.context;

public interface Context {
    Object get(Object key);

    void put(Object key, Object value);

    default <T> T get(Object key, Class<T> valueClass) {
        //noinspection unchecked
        return (T) get(key);
    }
}
