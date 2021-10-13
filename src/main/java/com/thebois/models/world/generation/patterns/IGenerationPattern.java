package com.thebois.models.world.generation.patterns;

/**
 * Pattern to use for generation.
 */
public interface IGenerationPattern {

    /**
     * Gets the frequency used for generation.
     * <p>
     * Should not be equals to 0.
     * </p>
     *
     * @return The frequency to be returned.
     */
    float getFrequency();

    /**
     * Gets the persistence used for generation.
     *
     * <p>
     * Should not be equals to 0.
     * </p>
     *
     * @return The persistence to be returned.
     */
    float getPersistence();

    /**
     * Gets the octave used for generation.
     * <p>
     * Should not be equals or less than 0.
     * </p>
     *
     * @return The octave to be returned.
     */
    int getOctave();

    /**
     * Gets the frequency used for generation.
     * <p>
     * Should not be equals to 0.
     * </p>
     *
     * @return The octave to be returned.
     */
    float getAmplitude();

}
