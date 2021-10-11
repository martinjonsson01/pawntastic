package com.thebois.models;

import java.util.Collection;
import java.util.Optional;

import com.thebois.models.world.structures.IStructure;

/**
 * Allows for locating of specific types of objects.
 */
public interface IFinder {

    /**
     * Finds and returns the closest IStructure to the passed position.
     *
     * @param position The position to find a structure closest to.
     *
     * @return The found IStructure.
     */
    Optional<IStructure> findNearestStructure(Position position);

    /**
     * Finds and returns the closest incomplete IStructure to the passed position.
     *
     * @param position The position to find an incomplete structure closest to.
     *
     * @return The found IStructure.
     */
    Optional<IStructure> findNearestIncompleteStructure(Position position);

    /**
     * Finds and returns a collection of length n of the closest IStructure to the passed position.
     *
     * @param position The position to find structures closest to.
     * @param count    Number of structures to find.
     *
     * @return The found IStructure.
     */
    Collection<IStructure> findNearestStructures(Position position, int count);

    /**
     * Returns the IStructure at position.
     *
     * @param position The position to check.
     *
     * @return The found IStructure at the specified position.
     */
    Optional<IStructure> getStructureAt(Position position);

}
