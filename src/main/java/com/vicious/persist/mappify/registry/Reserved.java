package com.vicious.persist.mappify.registry;

import java.util.HashSet;
import java.util.Set;

/**
 * A utility class for hard-coded reserved Persist meta-field names. These names cannot be used as names in an {@link com.vicious.persist.annotations.Save} annotation.
 * @author Jack Andersen
 * @since 1.0
 */
public class Reserved {
    private static final Set<String> reserved = new HashSet<String>();

    public static final String C_NAME = register("C_N_");
    public static final String E_NAME = register("E_N_");
    public static final String TRANSFORMER_VER = register("T_V_");

    public static String register(String name) {
        reserved.add(name);
        return name;
    }

    public static boolean isReserved(String name) {
        return reserved.contains(name);
    }

}
