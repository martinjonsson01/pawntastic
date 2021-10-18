package com.thebois.models.beings;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a collection of entities that cooperate and metadata about the group.
 */
public interface IBeingGroup extends Serializable {

    /**
     * Updates the internal state of the IBeingGroup.
     */
    void update();

    /**
     * Returns a collection of IBeings that belong to the IBeingGroup.
     *
     * @return The internal collection of IBeings.
     */
    Collection<IBeing> getBeings();

}
