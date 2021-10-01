package com.thebois.utils;

/**
 * Utilities for operating on strings.
 */
public final class StringUtils {

    private StringUtils() {

    }

    /**
     * Makes the first letter a capital letter.
     *
     * @param text The string to get a capital letter.
     *
     * @return A string with a capital letter.
     */
    public static String capitalizeFirst(final String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

}
