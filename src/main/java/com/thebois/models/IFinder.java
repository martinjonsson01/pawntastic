package com.thebois.models;

/**
 * Allows for locating of specific types of objects.
 *
 * @param <TFindable> The type to locate.
 */
public interface IFinder<TFindable> {

    /**
     * Finds and returns an object of the requested type.
     *
     * @return The found object.
     */
    TFindable find();

}
