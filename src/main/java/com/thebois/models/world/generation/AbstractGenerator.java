package com.thebois.models.world.generation;

import com.thebois.models.world.generation.patterns.IGenerationPattern;
import com.thebois.utils.PerlinNoiseGenerator;

/**
 * Generates noise with given settings.
 */
public abstract class AbstractGenerator {

    private final PerlinNoiseGenerator perlinNoiseGenerator;

    /**
     * Instantiate a generator with given pattern and seed.
     *
     * @param pattern The pattern used to generate.
     * @param seed    The seed used to generate the world.
     */
    public AbstractGenerator(final IGenerationPattern pattern, final int seed) {
        this.perlinNoiseGenerator = new PerlinNoiseGenerator();
        setGenerationPattern(pattern);
        setSeed(seed);
    }

    protected void setGenerationPattern(final IGenerationPattern pattern) {
        perlinNoiseGenerator.setOctaves(pattern.getOctave());
        perlinNoiseGenerator.setAmplitude(pattern.getAmplitude());
        perlinNoiseGenerator.setPersistence(pattern.getPersistence());
        perlinNoiseGenerator.setFrequency(pattern.getFrequency());
    }

    protected void setSeed(final int seed) {
        perlinNoiseGenerator.setSeed(seed);
    }

    protected float sample(final float x, final float y) {
        return perlinNoiseGenerator.perlinNoise(x, y);
    }

}
