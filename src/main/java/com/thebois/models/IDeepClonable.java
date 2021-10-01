package com.thebois.models;

/**
 * Forces implementing classes to create a deep clone method.
 *
 * @param <TType> The type of the cloned object.
 */
public interface IDeepClonable<TType> {

    /**
     * Creates a clone with no references to the original object.
     *
     * @return The deep clone object.
     */
    TType deepClone();

}
