package com.thebois.models.beings.pathfinding;

import java.util.Collection;

import com.thebois.models.Position;
import com.thebois.models.world.ITile;

/**
 * Represents a way of finding paths from one location to another.
 */
public interface IPathFinder {

    /**
     * Calculates a path from a position to a destination.
     *
     * @param from        The starting location
     * @param destination The location to find a path to
     *
     * @return The points that represent the path to take to get to the destination
     */
    Collection<Position> path(ITile from, ITile destination);

}
