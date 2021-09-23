package com.thebois.models.world;

/**
 * Represents a world containing tiles.
 */
public interface IWorld {

    /**
     * Gets the neighbouring tiles.
     *
     * @param tile The tile to find the neighbours of.
     *
     * @return The neighbours.
     */
    Iterable<ITile> getNeighboursOf(ITile tile);

}
