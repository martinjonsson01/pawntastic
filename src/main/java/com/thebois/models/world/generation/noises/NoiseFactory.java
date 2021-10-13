package com.thebois.models.world.generation.noises;

/**
 * Factory that creates noises that generates different kind of patterns.
 */
public final class NoiseFactory {

    private NoiseFactory() {

    }

    /**
     * Creates a Noise that generates large chunks.
     *
     * @return The noise to sample with.
     */
    public static INoise createLargeChunksNoise() {
        final float frequency = 0.05f;
        final float persistence = 1.0f;
        final int octave = 4;
        final float amplitude = 4.0f;
        return new PerlinNoise(octave, amplitude, frequency, persistence);
    }

    /**
     * Creates a Noise that generates small chunks.
     *
     * @return The noise to sample with.
     */
    public static INoise createSmallChunksNoise() {
        final float frequency = 0.5f;
        final float persistence = 1.0f;
        final int octave = 4;
        final float amplitude = 1.0f;
        return new PerlinNoise(octave, amplitude, frequency, persistence);
    }

    /**
     * Creates a Noise that generates isolated units.
     *
     * @return The noise to sample with.
     */
    public static INoise createVerySmallChunksNoise() {
        final float frequency = 1f;
        final float persistence = 1.0f;
        final int octave = 1;
        final float amplitude = 1.2f;
        return new PerlinNoise(octave, amplitude, frequency, persistence);
    }

    /**
     * Creates a Noise that generates small chunks.
     *
     * <p>
     * Sample method on the noise will only return 0.
     * </P>
     *
     * @return The noise to sample with.
     */
    public static INoise createFillMapNoise() {
        final float frequency = 0;
        final float persistence = 0;
        final int octave = 0;
        final float amplitude = 0;
        return new PerlinNoise(octave, amplitude, frequency, persistence);
    }

}
