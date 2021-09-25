package com.thebois.models.beings;

import java.util.Collection;

import com.thebois.models.world.structures.IStructure;

/**
 * Represents a collection of entities that cooperate and metadata about the group.
 */
public interface IBeingGroup {

    /**
     * Updates the internal state of the IBeingGroup.
     */
    void update();

    /**
     * Updates collection of known IStructures.
     *
     * @param structures New collection of IStructures.
     */
    void updateKnownStructures(Collection<IStructure> structures);

    /**
     * Returns a collection of IBeings that belong to the IBeingGroup.
     *
     * @return The internal collection of IBeings.
     */
    Collection<IBeing> getBeings();

}
