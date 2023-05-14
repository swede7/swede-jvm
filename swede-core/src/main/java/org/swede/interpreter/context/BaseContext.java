package org.swede.interpreter.context;

import java.util.HashMap;
import java.util.Map;

public class BaseContext implements Context {
    private final Map<Object, Object> map = new HashMap<>();

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        map.put(key, value);
    }
}
