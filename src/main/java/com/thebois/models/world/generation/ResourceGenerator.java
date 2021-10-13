package com.thebois.models.world.generation;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.world.generation.noises.INoise;
import com.thebois.models.world.generation.noises.NoiseFactory;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceFactory;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.utils.MatrixUtils;

/**
 * Generator used to generate resources for the world.
 */
public class ResourceGenerator extends AbstractGenerator {

    private final Map<ResourceType, INoise> resourceNoise;
    private final int seed;

    /**
     * Instantiate a Resource Generator with pre-made settings used for generating values.
     *
     * @param seed The seed used to generate resources in the world.
     */
    public ResourceGenerator(final int seed) {
        resourceNoise = new HashMap<>();
        resourceNoise.put(ResourceType.WATER, NoiseFactory.createLargeChunksNoise());
        resourceNoise.put(ResourceType.TREE, NoiseFactory.createSmallChunksNoise());
        this.seed = seed;
    }

    /**
     * Generates a Resource Matrix with the given world size.
     *
     * <p>
     * Some elements maybe be null, be careful when reading from matrix.
     * </p>
     *
     * @param worldSize The size of the world.
     *
     * @return The resource matrix.
     */
    public IResource[][] generateResourceMatrix(final int worldSize) {
        final IResource[][] resourceMatrix = new IResource[worldSize][worldSize];
        MatrixUtils.populateElements(resourceMatrix, (x, y) -> null);

        // Add all resource types to the world.
        for (final ResourceType type : ResourceType.values()) {
            setNoise(resourceNoise.get(type));
            final int newSeed = seed + type.getSeedPermutation();
            setSeed(newSeed);
            MatrixUtils.populateElements(resourceMatrix,
                                         (x, y) -> generateResource(resourceMatrix, type, x, y));
        }
        return resourceMatrix;
    }

    private IResource generateResource(
        final IResource[][] resourceMatrix, final ResourceType type, final int x, final int y) {
        if (resourceMatrix[y][x] == null) {
            final float height = sample(x, y);
            if (height >= type.getThreshold()) {
                return ResourceFactory.createResource(type, x, y);
            }
        }
        // Keep resource if position is occupied.
        return resourceMatrix[y][x];
    }

}