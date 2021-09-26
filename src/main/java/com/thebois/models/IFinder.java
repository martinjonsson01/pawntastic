package com.thebois.models;

import java.util.Optional;

import com.thebois.models.world.structures.IStructure;

/**
 * Allows for locating of specific types of objects.
 */
public interface IFinder {

    /**
     * Finds and returns the closest IStructure to the passed position.
     *
     * @param position The position to find a structure closest to
     *
     * @return The found IStructure.
     */
    Optional<IStructure> findNearestStructure(Position position);

    /**
     * Returns the IStructure at position.
     *
     * @param position The position to check
     *
     * @return The found IStructure else null.
     */
    Optional<IStructure> getStructureAt(Position position);

}
