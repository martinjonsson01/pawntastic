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

    /**
     * Traverses the given Optional matrix using a spiral pattern to find first present element.
     *
     * @param matrix Matrix to be searched
     *
     * @param startRow Row to start search from
     *
     * @param startCol Column to start search from
     *
     * @param maxSearchRadius Maximum search radius
     *
     * @param <TType> Generic type of Optional matrix
     *
     * @return First found present Optional element of type TType
     */
    public static <TType> Optional<TType> matrixSpiralSearch(final Optional<TType>[][] matrix,
                                                       final int startRow,
                                                       final int startCol,
                                                       final int maxSearchRadius) {
        // Spiral search pattern
        final int[][] searchPattern = {
            {0, 0}, {0, 1}, {1, 1},
            {1, 0}, {1, -1}, {0, -1},
            {-1, -1}, {-1, 0}, {-1, 1},
            };

        for (int searchRadius = 1; searchRadius <= maxSearchRadius; searchRadius++) {
            for (final int[] ints : searchPattern) {

                try {
                    final int searchRow = startRow + ints[0] * searchRadius;
                    final int searchCol = startCol + ints[1] * searchRadius;

                    final Optional<TType> currentElem =
                        matrix[searchRow][searchCol];

                    if (currentElem.isPresent()) {
                        return matrix[searchRow][searchCol];
                    }
                }
                catch (final ArrayIndexOutOfBoundsException exception) {
                    // Since there is a search limit, we can let the method loop outside of matrix
                }
            }
        }

        // If no structure was found
        return Optional.empty();
    }

}
