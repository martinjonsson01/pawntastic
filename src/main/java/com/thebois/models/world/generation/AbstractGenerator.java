package com.thebois.models.world.generation;

import com.thebois.models.world.generation.noises.INoise;

/**
 * Generator that uses noise.
 *
 * @author Mathias
 */
public abstract class AbstractGenerator {

    private INoise noise;
    private final int seed;
    private final int worldSize;

    /**
     * Instantiate with a World Size and Seed used for generation.
     *
     * @param worldSize The size of the world to generate.
     * @param seed      The seed used to generate the world.
     */
    protected AbstractGenerator(final int worldSize, final int seed) {
        this.seed = seed;
        this.worldSize = worldSize;
    }

    protected int getSeed() {
        return seed;
    }

    protected int getWorldSize() {
        return worldSize;
    }

    protected void setNoise(final INoise noise) {
        this.noise = noise;
    }

    protected void setSeed(final int seed) {
        noise.setSeed(seed);
    }

    protected float sample(final float x, final float y) {
        return noise.sample(x, y);
    }

}
