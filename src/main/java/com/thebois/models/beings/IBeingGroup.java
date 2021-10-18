package com.thebois.models.beings;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a collection of entities that cooperate and metadata about the group.
 */
public interface IBeingGroup extends Serializable {

    /**
     * Updates the internal state of the IBeingGroup.
     *
     * @param deltaTime The amount of time that has passed since the last update, in seconds.
     */
    void update(float deltaTime);

    /**
     * Returns a collection of IBeings that belong to the IBeingGroup.
     *
     * @return The internal collection of IBeings.
     */
    Collection<IBeing> getBeings();

}
