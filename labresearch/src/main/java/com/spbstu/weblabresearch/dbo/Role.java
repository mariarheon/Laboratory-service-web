package com.spbstu.weblabresearch.dbo;

import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum Role implements GrantedAuthority {
    ADMIN("Администратор"),
    ASSISTANT("Лаборант"),
    CLIENT("Клиент");

    private final String asString;

    Role(String roleStr) {
        this.asString = roleStr;
    }

    @Override
    public String toString() {
        return asString;
    }

    private static final Map<String, Role> LOOKUP_MAP = new HashMap<>();

    static {
        for (Role v : values()) {
            LOOKUP_MAP.put(v.asString, v);
        }
    }

    public static Role getByStr(String roleStr) {
        return LOOKUP_MAP.get(roleStr);
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
