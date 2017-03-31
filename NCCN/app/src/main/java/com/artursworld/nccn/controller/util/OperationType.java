package com.artursworld.nccn.controller.util;


import java.util.HashMap;
import java.util.Map;

public enum OperationType {

    PRE, POST, FOLLOW_UP;

    private static final Map<String, OperationType> lookupByName = new HashMap<String, OperationType>();

    static {
        for (OperationType g : values()) {
            lookupByName.put(g.name(), g);
        }
    }

    public static OperationType findByName(String name) {
        name = name.toUpperCase();
        return lookupByName.get(name);
    }
}
