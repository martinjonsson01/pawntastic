package com.thebois.models;

import com.thebois.models.tiles.ITile;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IFinder {

    private ITile[][] worldMatrix;

    private World() {
    }

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     */
    public World(int worldSize) {
        worldMatrix = new ITile[worldSize][worldSize];
    }

    /**
     * Locates an object in the world and returns it.
     *
     * @return Object.
     */
    public Object find() {
        return null;
    }

    /**
     * Returns the matrix of the world.
     *
     * @return ITile[][]
     */
    public ITile[][] getWorld() {
        return worldMatrix;
    }

}
