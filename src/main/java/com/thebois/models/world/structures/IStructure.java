package com.thebois.models.world.structures;

import com.thebois.abstractions.IDeepClonable;
import com.thebois.models.world.ITile;

/**
 * Represents a Structure. E.g. a house.
 */
public interface IStructure extends ITile, IBuildable, IDeepClonable<IStructure> {

    /**
     * Returns the specific type of terrain.
     *
     * @return The Structure type.
     */
    StructureType getType();

}
