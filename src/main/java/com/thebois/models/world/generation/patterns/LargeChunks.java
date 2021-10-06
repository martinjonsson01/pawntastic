package com.thebois.models.world.generation.patterns;

/**
 * Pattern used to generate larger chunks.
 */
public class LargeChunks implements IGenerationPattern {

    @Override
    public float getFrequency() {
        final float frequency = 0.05f;
        return frequency;
    }

    @Override
    public float getPersistence() {
        final float persistence = 1.0f;
        return persistence;
    }

    @Override
    public int getOctave() {
        final int octave = 4;
        return octave;
    }

    @Override
    public float getAmplitude() {
        final float amplitude = 4.0f;
        return amplitude;
    }

}
