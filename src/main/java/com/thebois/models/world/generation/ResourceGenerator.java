package com.thebois.models.world.generation;

import java.util.Optional;

import com.thebois.models.world.generation.patterns.LargeChunks;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.Water;

/**
 * Generator used to generate resources for the world.
 */
public class ResourceGenerator extends AbstractGenerator {

    private static final float WATER_THRESHOLD = 0.5f;

    /**
     * Instantiate a Resource Generator with pre-made settings used for generating values.
     *
     * @param seed The seed used to generate resources in the world.
     */
    public ResourceGenerator(final int seed) {
        super(new LargeChunks(), seed);
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

                final float height = sample(x, y);
                if (height > WATER_THRESHOLD) {
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
