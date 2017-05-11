package com.artursworld.nccn.model.entity;


import java.util.HashMap;
import java.util.Map;

public enum Gender {

    MALE, FEMALE, UNKNOWN;

    private static final Map<String, Gender> lookupByName = new HashMap<>();

    static {
        for (Gender gender : values()) {
            lookupByName.put(gender.name(), gender);
        }
    }

    public static Gender findByName(String name) {
        if (name != null)
            return lookupByName.get(name.toUpperCase());
        return UNKNOWN;
    }
}
