package com.thebois.models.world;

import com.thebois.models.Position;

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

    /**
     * Gets the tile at a given location.
     *
     * @param position The position to get the tile from.
     *
     * @return The tile at the given position.
     */
    ITile getTileAt(Position position);

    /**
     * Gets the tile at a given location.
     *
     * @param posX The x-coordinate to get the tile from.
     * @param posY The y-coordinate to get the tile from.
     *
     * @return The tile at the given position.
     */
    ITile getTileAt(int posX, int posY);

    /**
     * Finds a vacant spot and returns it.
     *
     * @return The vacant tile.
     */
    ITile getRandomVacantSpot();

}
