package com.thebois.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Utilities for operating on matrices.
 *
 * @author Martin
 * @author Mathias
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

}
