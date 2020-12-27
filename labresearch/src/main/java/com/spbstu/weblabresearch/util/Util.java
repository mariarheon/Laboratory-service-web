package com.spbstu.weblabresearch.util;

import java.text.SimpleDateFormat;

/**
 *
 */
public final class Util {
    private Util() {
        /* nothing */
    }

    private static final SimpleDateFormat dtFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static final SimpleDateFormat dFormat = new SimpleDateFormat("dd.MM.yyyy");


    public static String toDTString(java.util.Date date) {
        return dtFormat.format(date);
    }

    public static String toDString(java.util.Date date) {
        return dFormat.format(date);
    }
}
