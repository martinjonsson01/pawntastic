package com.thebois.models;

import java.util.Optional;

import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;

/**
 * Allows for locating of specific types of objects.
 */
public interface IStructureFinder {

    /**
     * Finds and returns the closest IStructure to the passed position.
     *
     * @param origin The position to find a structure closest to.
     * @param type The type of structure to find.
     *
     * @return The found IStructure.
     */
    Optional<IStructure> getNearbyStructureOfType(Position origin, StructureType type);

    /**
     * Finds and returns the closest incomplete IStructure to the passed position.
     *
     * @param origin The position to find an incomplete structure closest to.
     *
     * @return The found IStructure.
     */
    Optional<IStructure> getNearbyIncompleteStructure(Position origin);

}
