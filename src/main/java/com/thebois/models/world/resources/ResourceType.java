package com.thebois.models.world.resources;

/**
 * All types of resources.
 */
public enum ResourceType {
    /**
     * Water Resource.
     */
    WATER(0.5f, 0),
    /**
     * Tree Resource.
     */
    TREE(0.5f, 31);
    private final float threshold;
    private final int seedPermutation;

    ResourceType(final float threshold, final int seedPermutation) {
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
