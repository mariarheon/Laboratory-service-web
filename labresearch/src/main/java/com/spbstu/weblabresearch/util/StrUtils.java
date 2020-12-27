package com.spbstu.weblabresearch.util;

/**
 *
 */
public final class StrUtils {
    private StrUtils() {
        /* nothing */
    }

    public static String removeStart(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}

