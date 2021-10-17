package com.thebois.utils;

import java.util.ArrayList;
import java.util.Collection;
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
     * @param matrix     Matrix to be transformed.
     * @param <TElement> Type of matrix element.
     *
     * @return Returns the matrix in collection form.
     */
    public static <TElement> Collection<TElement> toCollection(
        final TElement[][] matrix) {
        final Collection<TElement> matrixCollection = new ArrayList<>();
        MatrixUtils.forEachElement(matrix, element -> {
            if (element != null) {
                matrixCollection.add(element);
            }
        });
        return matrixCollection;
    }

    /**
     * Iterates a matrix like a list, going from top left element to bottom right.
     *
     * @param matrix     The matrix to operate on.
     * @param action     The action to perform for each element.
     * @param <TElement> The type of the matrix elements.
     */
    public static <TElement> void forEachElement(
        final TElement[][] matrix, final Consumer<TElement> action) {
        for (final TElement[] row : matrix) {
            for (final TElement element : row) {
                action.accept(element);
            }
        }
    }

    /**
     * Populate matrix with elements.
     *
     * @param matrix          The matrix to be populated.
     * @param elementSupplier The supplier used to create elements.
     * @param <TElement>      The type of element to create.
     */
    public static <TElement> void populateElements(
        final TElement[][] matrix, final IMatrixSupplier<TElement> elementSupplier) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                matrix[y][x] = elementSupplier.createElementAt(x, y);
            }
        }
    }

    /**
     * Traverses the given Optional matrix using a spiral pattern to find present element
     * within the search radius.
     *
     * @param matrix Matrix to be searched.
     * @param startRow Row to start search from.
     * @param startCol Column to start search from.
     * @param maxSearchRadius Maximum search radius.
     * @param maxFoundElements Maximum needed elements to be found.
     * @param <TType> Generic type of Optional matrix.
     *
     * @return First found elements of type TType.
     */
    public static <TType> Collection<TType> matrixSpiralSearch(
        final TType[][] matrix,
        final int startRow,
        final int startCol,
        final int maxSearchRadius,
        final int maxFoundElements) {

        final Collection<TType> foundElements = new ArrayList<>();

        for (int length = 0; length < maxSearchRadius; length++) {
            for (int row = -length; row <= length * 2; row++) {

                for (int col = -length; col <= length * 2; col++) {
                    try {
                        if (matrix[startRow + row][startCol + col] != null) {
                            foundElements.add(matrix[startRow + row][startCol + col]);

                            if (foundElements.size() > maxFoundElements) {
                                return foundElements;
                            }
                        }
                    }
                    catch (final ArrayIndexOutOfBoundsException exception) {
                        // Since there is a search limit, we can let the method loop
                        // outside of matrix.
                    }
                }
            }
        }
        return foundElements;
    }

}
