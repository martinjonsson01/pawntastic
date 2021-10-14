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

    /**
     * Threshold value used for terrain generation.
     * <p>
     * Used together with some kind of value generator and if the threshold is over, under or equal
     * to the generated value then the terrain should be created.
     * </p>
     *
     * <p>
     * Should be used like this: Value >= Threshold or Value <= Threshold.
     * </p>
     *
     * @return The Threshold..
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * Seed permutation used for terrain generation.
     *
     * <p>
     * Used to offset the terrain from other terrain.
     * </p>
     * <p>
     * Should be used like this: BaseSeed "Some Operation" seedPermutation. Make sure to use the
     * same operation for all terrain in order to not get unwanted results.
     * </p>
     *
     * @return The seed permutation.
     */
    public int getSeedPermutation() {
        return seedPermutation;
    }
}
