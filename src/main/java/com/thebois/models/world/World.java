package com.thebois.models.world;

import com.thebois.models.IFinder;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IFinder {

    private ITerrain[][] terrainMatrix;

    private World() {
    }

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     */
    public World(int worldSize) {
        terrainMatrix = new ITerrain[worldSize][worldSize];
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
    public ITerrain[][] getWorld() {
        return terrainMatrix;
    }

}
