package com.thebois.models.beings;

import java.util.Collection;

import com.thebois.models.Position;

/**
 * Represents a collection of entities that cooperate and metadata about the group.
 */
public interface IBeingGroup {

    Collection<IBeing> getBeings();

    void createBeing();

    void createBeing(Position spawnPosition);

    void createBeing(Position spawnPosition, int numberOfNewBeings);

}
