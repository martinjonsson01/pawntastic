package com.thebois.models.beings;

import java.util.Collection;

import com.thebois.models.Position;

/**
 * Represents a collection of entities that cooperate and metadata about the group.
 */
public interface IBeingGroup {

    /**
     * Updates the internal state of the IBeingGroup.
     */
    void update();

    /**
     * Returns a collection of IBeings that belong to the IBeingGroup.
     */
    Collection<IBeing> getBeings();

}
