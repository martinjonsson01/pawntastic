package com.thebois.models.world.generation.noises;

/**
 * Noise used to generate a value with given X and Y value.
 *
 * @author Mathias
 */
public interface INoise {

    /**
     * Generates a float value from given values.
     *
     * @param x X value used to sample.
     * @param y Y value used to sample.
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
