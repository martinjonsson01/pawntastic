package com.thebois.utils;

import java.util.Random;

/**
 * Utilities for generation of random numbers.
 */
public final class RandomUtils {

    private RandomUtils() {

    }

    /**
     * Returns a pseudo-random number between min and max, inclusive. The difference between min and
     * max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param random The generator to use for random values.
     * @param min    Minimum value
     * @param max    Maximum value.  Must be greater than min.
     *
     * @return Integer between min and max, inclusive.
     *
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(final Random random, final int min, final int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return random.nextInt((max - min) + 1) + min;
    }

}
