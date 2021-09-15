package com.thebois.models.beings;

import java.util.Collection;

/**
 * Represents a collection of entities that cooperate and metadata about the group.
 */
public interface IBeingGroup {

    Collection<IBeing> getBeings();

}
