package com.thebois.models;

/**
 * Allows for locating of specific types of objects.
 * @param <T> The type to locate.
 */
public interface IFinder<T> {

    T find();

}
