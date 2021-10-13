package com.thebois.models.world.terrains;

/**
 * All the different types of terrain in the game.
 */
public enum TerrainType {
    /**
     * The default ground terrain.
     */
    GRASS(0, 0),
    /**
     * Terrain of type dirt.
     */
    DIRT(0.4f, 27),
    /**
     * Terrain of type sand.
     */
    SAND(0.3f, 0);
    private final float threshold;
    private final int seedPermutation;

    TerrainType(final float threshold, final int seedPermutation) {
        this.threshold = threshold;
        this.seedPermutation = seedPermutation;
    }

    public float getThreshold() {
        return threshold;
    }

    public int getSeedPermutation() {
        return seedPermutation;
    }
}
