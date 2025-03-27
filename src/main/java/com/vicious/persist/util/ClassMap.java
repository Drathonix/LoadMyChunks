package com.vicious.persist.util;

import java.util.IdentityHashMap;

/**
 * A {@link java.util.Map} that always uses {@link Class} instances as keys.
 * Class instances are Constable and therefore can use an IdentityHashMap.
 * @author Jack Andersen
 */
public class ClassMap<V> extends IdentityHashMap<Class<?>,V> {
}
