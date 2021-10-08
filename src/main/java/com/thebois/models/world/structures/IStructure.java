package com.thebois.models.world.structures;

import java.io.Serializable;

import com.thebois.models.IDeepClonable;
import com.thebois.models.world.ITile;

/**
 * Represents a Structure. E.g. a house.
 */
public interface IStructure extends ITile, IDeepClonable<IStructure>, Serializable {

    /**
     * Returns the specific type of terrain.
     *
     * @return The Structure type.
     */
    StructureType getType();

}
