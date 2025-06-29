package com.vicious.persist.util;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * A {@link java.util.Map} that always uses {@link java.lang.Class} instances as keys.
 * Class instances are Constable and therefore can use an IdentityHashMap.
 * @author Jack Andersen
 * @param <V> The value type.
 */
public class ClassMap<V> extends IdentityHashMap<Class<?>,V> {

    /**
     * Creates an empty ClassMap
     */
    public ClassMap() {
    }

    /**
     * Creates an empty ClassMap and allocates space for use later.
     * @param expectedMaxSize the expected size of the map.
     */
    public ClassMap(int expectedMaxSize) {
        super(expectedMaxSize);
    }

    /**
     * Creates a ClassMap containing the contents of m.
     * @param m the map to copy from.
     */
    public ClassMap(Map<? extends Class<?>, ? extends V> m) {
        super(m);
    }
}
