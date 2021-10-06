package com.thebois.models.world.generation.patterns;

/**
 * Pattern to use for generation.
 */
public interface IGenerationPattern {

    /**
     * Gets the frequency used for generation.
     *
     * @return The frequency to be returned.
     */
    float getFrequency();

    /**
     * Gets the persistence used for generation.
     *
     * @return The persistence to be returned.
     */
    float getPersistence();

    /**
     * Gets the octave used for generation.
     *
     * @return The octave to be returned.
     */
    int getOctave();

    /**
     * Gets the frequency used for generation.
     *
     * @return The octave to be returned.
     */
    float getAmplitude();

}
