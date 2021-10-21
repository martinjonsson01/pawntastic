package com.thebois.models.world;

import java.io.Serializable;
import java.util.Optional;

import com.thebois.models.Position;

/**
 * Represents a world containing tiles.
 */
public interface IWorld extends Serializable {

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
     * @param x The x-coordinate to get the tile from.
     * @param y The y-coordinate to get the tile from.
     *
     * @return The tile at the given position.
     */
    ITile getTileAt(int x, int y);

    /**
     * Finds a randomly generated vacant spot inside the radius of the given origin and returns it.
     *
     * @param origin Where to center the random locations around.
     * @param radius The search radius in which to randomly find a vacant spot.
     *
     * @return The vacant tile.
     */
    ITile getRandomVacantSpotInRadiusOf(Position origin, int radius);

    /**
     * Finds an empty neighbour of a tile that is closest to the given position, if there is any.
     *
     * @param tile The tile to get neighbours of.
     * @param from The position that needs to be closest to the returned neighbour.
     *
     * @return The position of the neighbouring tile that is the closest to the given position, if
     *     it exists.
     */
    Optional<Position> getClosestNeighbourOf(ITile tile, Position from);

}
