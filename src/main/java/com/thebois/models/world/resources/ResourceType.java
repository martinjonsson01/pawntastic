package com.thebois.models.world.resources;

/**
 * All types of resources.
 */
public enum ResourceType {
    /**
     * A body of water.
     */
    WATER(0.5f, 0),
    /**
     * A natural tree.
     */
    TREE(0.5f, 31),
    /**
     * A large stone on the ground.
     */
    STONE(0.5f, 27);
    private final float threshold;
    private final int seedPermutation;

    ResourceType(final float threshold, final int seedPermutation) {
        this.threshold = threshold;
        this.seedPermutation = seedPermutation;
    }

    /**
     * Threshold value used for resource generation.
     * <p>
     * Used together with some kind of value generator and if the threshold is over, under or equal
     * to the generated value then the resource should be created.
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
     * Seed permutation used for resource generation.
     *
     * <p>
     * Used to offset the resource from other resources.
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
