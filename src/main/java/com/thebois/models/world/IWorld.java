package com.thebois.models.world;

import java.util.Optional;

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

    /**
     * Finds an empty neighbour of a tile that is closest to the given position.
     *
     * @param tile The tile to get neighbours of.
     * @param from The position that needs to be closest to the returned neighbour.
     *
     * @return The position of the neighbouring tile that is the closest to the given position.
     */
    Optional<Position> getClosestNeighbourOf(ITile tile, Position from);

}
