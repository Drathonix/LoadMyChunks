package com.vicious.persist.mappify.registry;

import java.util.HashSet;
import java.util.Set;

public class Reserved {
    private static final Set<String> reserved = new HashSet<String>();

    public static final String C_NAME = register("C_N_");
    public static final String E_NAME = register("E_N_");

    public static String register(String name) {
        reserved.add(name);
        return name;
    }

    public static boolean isReserved(String name) {
        return reserved.contains(name);
    }

}
