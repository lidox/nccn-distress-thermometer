package com.artursworld.nccn.model.entity;

import java.util.HashMap;
import java.util.Map;

public enum PsychoSocialSupportState {

    NOT_ASKED, ACCEPTED, REJECTED;

    private static final Map<String, PsychoSocialSupportState> lookupByName = new HashMap<>();

    static {
        for (PsychoSocialSupportState state : values()) {
            lookupByName.put(state.name(), state);
        }
    }

    public static PsychoSocialSupportState findByName(String name) {
        return lookupByName.get(name.toUpperCase());
    }
}
