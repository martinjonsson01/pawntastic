package com.thebois.models.world.generation.noises;

/**
 * Generator used to generate value with given X and Y value.
 */
public interface INoise {

    /**
     * Generates a float value with given values.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     *
     * @return The float value.
     */
    float sample(float x, float y);

    /**
     * Sets the seed the noise uses to generate sample.
     *
     * @param seed The seed to be used.
     */
    void setSeed(int seed);

}
