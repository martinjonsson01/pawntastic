package com.thebois.models;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IFinder {

    private int[][] worldMatrix;

    private World() {
    }

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     */
    public World(int worldSize) {
        worldMatrix = new int[worldSize][worldSize];
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
     * @return int[][]
     */
    public int[][] getWorld() {
        return worldMatrix;
    }

}
