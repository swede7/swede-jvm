package org.swede.core.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class StepParameterMapper {
    private StepParameterMapper() {

    }

    private static final Map<Class<?>, Function<String, ?>> MAPPERS = new HashMap<>();

    static {
        MAPPERS.put(String.class, Function.identity());
        MAPPERS.put(Integer.class, Integer::parseInt);
        MAPPERS.put(int.class, Integer::parseInt);
        MAPPERS.put(Double.class, Double::parseDouble);
        MAPPERS.put(double.class, Double::parseDouble);
        MAPPERS.put(Float.class, Float::parseFloat);
        MAPPERS.put(float.class, Float::parseFloat);
    }

    public static boolean supportedType(Class<?> parameterType) {
        return MAPPERS.containsKey(parameterType);
    }

    public static <T> T map(String parameter, Class<T> parameterType) {
        //noinspection unchecked
        return (T) MAPPERS.get(parameterType).apply(parameter);
    }
}
