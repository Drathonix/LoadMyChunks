package com.vicious.persist.util;

import java.lang.reflect.Array;

/**
 * Handles situations involving auto-unboxable objects and classes.
 * @author Jack Andersen
 * @since 1.3.0
 */
public class Boxing {
    public static Class<?> requireObjective(Class<?> cls){
        if(cls == boolean.class) {
            return Boolean.class;
        }
        if(cls == char.class){
            return Character.class;
        }
        if(cls == byte.class){
            return Byte.class;
        }
        if(cls == short.class){
            return Short.class;
        }
        if(cls == int.class){
            return Integer.class;
        }
        if(cls == long.class){
            return Long.class;
        }
        if(cls == float.class){
            return Float.class;
        }
        if(cls == double.class){
            return Double.class;
        }
        return cls;
    }

    public static void arraySet(Object arrayOut, int i, Object o) {
        if(arrayOut.getClass().getComponentType().isPrimitive()) {
            if (o instanceof Boolean) {
                Array.setBoolean(arrayOut, i, (Boolean) o);
            } else if (o instanceof Character) {
                Array.setChar(arrayOut, i, (Character) o);
            } else if (o instanceof Byte) {
                Array.setByte(arrayOut, i, (Byte) o);
            } else if (o instanceof Short) {
                Array.setShort(arrayOut, i, (Short) o);
            } else if (o instanceof Integer) {
                Array.setInt(arrayOut, i, (Integer) o);
            } else if (o instanceof Long) {
                Array.setLong(arrayOut, i, (Long) o);
            } else if (o instanceof Float) {
                Array.setFloat(arrayOut, i, (Float) o);
            } else if (o instanceof Double) {
                Array.setDouble(arrayOut, i, (Double) o);
            }
        }
        else {
            Array.set(arrayOut, i, o);
        }
    }
}
