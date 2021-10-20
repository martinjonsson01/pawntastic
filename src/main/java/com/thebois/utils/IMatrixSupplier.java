package com.thebois.utils;

/**
 * Supplier of elements in a matrix.
 *
 * @param <TElement> The type of element in the matrix.
 *
 * @author Mathias
 */
public interface IMatrixSupplier<TElement> {

    /**
     * Generates an element at the given indices.
     *
     * @param x X index of the matrix.
     * @param y Y index of the matrix.
     *
     * @return The created element.
     */
    TElement createElementAt(int x, int y);

}
