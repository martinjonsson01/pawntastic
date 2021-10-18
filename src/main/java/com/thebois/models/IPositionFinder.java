package com.thebois.models;

/**
 * Allows for locating certain types of positions in the world.
 */
public interface IPositionFinder {

    /**
     * Creates a list of tiles that are not occupied by a structure or resource.
     *
     * @param count The amount of empty positions that needs to be found.
     *
     * @return List of empty positions.
     */
    Iterable<Position> findEmptyPositions(int count);

}
