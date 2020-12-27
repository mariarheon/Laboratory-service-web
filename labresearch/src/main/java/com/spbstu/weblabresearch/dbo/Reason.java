package com.spbstu.weblabresearch.dbo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum Reason {
    COLLECTION("сбор"),
    RESEARCH("исследование");

    private final String asString;

    Reason(String str) {
        this.asString = str;
    }

    @Override
    public String toString() {
        return asString;
    }

    private static final Map<String, Reason> LOOKUP_MAP = new HashMap<>();

    static {
        for (Reason v : values()) {
            LOOKUP_MAP.put(v.toString(), v);
        }
    }

    public static Reason getByStr(String str) {
        return LOOKUP_MAP.get(str);
    }
}
