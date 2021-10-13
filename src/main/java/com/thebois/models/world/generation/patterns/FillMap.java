package com.thebois.models.world.generation.patterns;

/**
 * Pattern that make the generator return the same value everywhere.
 */
public class FillMap implements IGenerationPattern {

    @Override
    public float getFrequency() {
        return 0;
    }

    @Override
    public float getPersistence() {
        return 0;
    }

    @Override
    public int getOctave() {
        return 0;
    }

    @Override
    public float getAmplitude() {
        return 0;
    }

}
