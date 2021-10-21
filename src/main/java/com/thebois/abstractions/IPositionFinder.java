package com.thebois.abstractions;

import com.thebois.models.Position;

/**
 * Allows for locating certain types of positions in the world.
 */
public interface IPositionFinder {

    /**
     * Creates a list of positions that are not occupied.
     *
     * @param count The amount of empty positions that needs to be found.
     *
     * @return List of empty positions.
     */
    Iterable<Position> findEmptyPositions(int count);

    /**
     * Creates a list of positions that are not occupied.
     *
     * @param position The position to check around.
     * @param maxCount The max number of empty positions that needs to be found.
     * @param radius The distance around the given position to look for vacant positions.
     *
     * @return List of empty positions.
     */
    Iterable<Position> tryGetEmptyPositionsNextTo(Position position, int maxCount, float radius);

}
