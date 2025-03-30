package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to indicate Generic types on savable fields. This is required for Reflection to be able to retrieve generics.
 * Generics should be listed in the order read from left to right.
 * Examples :
 * {@literal
 * @Save
 * @Typing(String.class)
 * List<String> list = new ArrayList<>();
 * @Save
 * @Typing(String.class,Integer.class)
 * Map<String,Integer> map = new HashMap<>();
 * @Save
 * @Typing(String.class,ArrayList.class,ArrayList.class,HashMap.class,Integer.class,Double.class)
 * Map<String,ArrayList<ArrayList<HashMap<Integer,Double>>>> map = new HashMap<>();
 * }
 * @since 1.0
 * @author Jack Andersen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Typing {
    /**
     * @return the generic classes in order.
     */
    Class<?>[] value();
}
