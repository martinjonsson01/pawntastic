package com.thebois.models.world.generation;

import java.util.Optional;
import java.util.Random;

import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.Water;
import com.thebois.utils.PerlinNoiseGenerator;

/**
 * Generator used to generate resources for the world.
 */
public class ResourceGenerator {

    private static final float DIRT_THRESHOLD = 0.5f;
    private final PerlinNoiseGenerator resourceGenerator;

    /**
     * Instantiate a Terrain Generator with pre-made settings used for generating values.
     */
    public ResourceGenerator() {
        final Random random = new Random();
        final int octaves = 1;
        final float amplitude = 4f;
        final float frequency = 0.1f;
        final float persistence = 1.0f;
        final int seed = random.nextInt(Integer.MAX_VALUE);
        resourceGenerator = new PerlinNoiseGenerator(octaves,
                                                     amplitude,
                                                     frequency,
                                                     persistence,
                                                     seed);
    }

    /**
     * Generates a Resource Matrix with the given world size.
     *
     * @param worldSize The size of the world.
     *
     * @return The resource matrix.
     */
    public Optional<IResource>[][] generateResourceMatrix(final int worldSize) {
        final Optional<IResource>[][] resourceMatrix = new Optional[worldSize][worldSize];
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {

                final float height = resourceGenerator.perlinNoise(x, y);
                if (height > DIRT_THRESHOLD) {
                    resourceMatrix[y][x] = Optional.of(new Water(x, y));
                }
                else {
                    resourceMatrix[y][x] = Optional.empty();
                }
            }
        }
        return resourceMatrix;
    }

}
