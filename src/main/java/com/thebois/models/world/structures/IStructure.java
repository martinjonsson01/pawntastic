package com.thebois.models.world.structures;

import com.thebois.models.world.ITile;

/**
 * Represents a Structure. E.g. a house.
 */
public interface IStructure extends ITile {

    /**
     * Returns the specific type of terrain.
     *
     * @return The terrain type.
     */
    StructureType getType();
}
