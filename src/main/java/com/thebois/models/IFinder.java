package com.thebois.models;

/**
 * Allows for locating of specific types of objects.
 *
 * @param <T> The type to locate.
 */
public interface IFinder<T> {

    /**
     * Finds and returns an object of the requested type.
     *
     * @return The found object.
     */
    T find();

}
