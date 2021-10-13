package com.thebois.models.world.generation;

import com.thebois.models.world.generation.noises.INoise;

/**
 * Generates noise with given settings.
 */
public abstract class AbstractGenerator {

    private INoise noise;

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
