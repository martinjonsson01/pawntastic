package com.thebois.models.world.generation;

import com.thebois.models.world.generation.patterns.IGenerationPattern;
import com.thebois.utils.PerlinNoise;

/**
 * Generates noise with given settings.
 */
public abstract class AbstractGenerator {

    private final PerlinNoise perlinNoise;

    /**
     * Instantiate a generator with given pattern and seed.
     *
     * @param pattern The pattern used to generate.
     * @param seed    The seed used to generate the world.
     */
    public AbstractGenerator(final IGenerationPattern pattern, final int seed) {
        this.perlinNoise = new PerlinNoise();
        setGenerationPattern(pattern);
        setSeed(seed);
    }

    protected void setGenerationPattern(final IGenerationPattern pattern) {
        perlinNoise.setSettings(pattern);
    }

    protected void setSeed(final int seed) {
        perlinNoise.setSeed(seed);
    }

    protected float sample(final float x, final float y) {
        return perlinNoise.sample(x, y);
    }

}
