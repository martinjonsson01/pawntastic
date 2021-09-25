package com.thebois.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Utilities for operating on matrices.
 */
public final class MatrixUtils {

    private MatrixUtils() {

    }

    /**
     * Turns a matrix into a Collection of type TElement.
     *
     * @param matrix     Matrix to be transformed
     * @param <TElement> Type of matrix element
     *
     * @return Returns the matrix in collection form
     */
    public static <TElement> Collection<TElement> matrixToCollection(
        final Optional<TElement>[][] matrix) {
        final Collection<TElement> matrixCollection = new ArrayList<>();
        MatrixUtils.forEachElement(matrix, elem -> elem.ifPresent(matrixCollection::add));
        return matrixCollection;
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
