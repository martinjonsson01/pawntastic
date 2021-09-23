package com.thebois.utils;

import java.util.function.Consumer;

/**
 * Utilities for operating on matrices.
 */
public final class MatrixUtils {

    private MatrixUtils() {

    }

    /**
     * Iterates a matrix like a list, going from top left element to bottom right.
     *
     * @param matrix     The matrix to operate on
     * @param action     The action to perform for each element
     * @param <TElement> The type of the matrix elements
     */
    public static <TElement> void forEachElement(final TElement[][] matrix,
                                                 final Consumer<TElement> action) {
        for (final TElement[] row : matrix) {
            for (final TElement element : row) {
                action.accept(element);
            }
        }
    }

}
