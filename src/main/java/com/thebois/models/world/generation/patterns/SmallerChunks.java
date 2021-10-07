package com.thebois.models.world.generation.patterns;

/**
 * Pattern used to generate smaller chunks.
 */
public class SmallerChunks implements IGenerationPattern {

    @Override
    public float getFrequency() {
        final float frequency = 0.5f;
        return frequency;
    }

    @Override
    public float getPersistence() {
        final float persistence = 0.5f;
        return persistence;
    }

    @Override
    public int getOctave() {
        final int octave = 3;
        return octave;
    }

    @Override
    public float getAmplitude() {
        final float amplitude = 3f;
        return amplitude;
    }

}
