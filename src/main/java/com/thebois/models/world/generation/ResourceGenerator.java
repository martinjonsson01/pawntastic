package com.thebois.models.world.generation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.thebois.models.world.generation.patterns.IGenerationPattern;
import com.thebois.models.world.generation.patterns.LargeChunks;
import com.thebois.models.world.generation.patterns.SmallerChunks;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceFactory;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.utils.MatrixUtils;

/**
 * Generator used to generate resources for the world.
 */
public class ResourceGenerator extends AbstractGenerator {

    private static final float WATER_THRESHOLD = 0.5f;
    private static final float TREE_THRESHOLD = 0.4f;
    private static final Map<ResourceType, IGenerationPattern> RESOURCE_PATTERN;
    private static final Map<ResourceType, Float> RESOURCE_THRESHOLD;

    static {
        RESOURCE_PATTERN = new HashMap<>();
        RESOURCE_PATTERN.put(ResourceType.WATER, new LargeChunks());
        RESOURCE_PATTERN.put(ResourceType.TREE, new SmallerChunks());
    }

    static {
        RESOURCE_THRESHOLD = new HashMap<>();
        RESOURCE_THRESHOLD.put(ResourceType.WATER, WATER_THRESHOLD);
        RESOURCE_THRESHOLD.put(ResourceType.TREE, TREE_THRESHOLD);
    }

    private int seed;

    /**
     * Instantiate a Resource Generator with pre-made settings used for generating values.
     *
     * @param seed The seed used to generate resources in the world.
     */
    public ResourceGenerator(final int seed) {
        super(new LargeChunks(), seed);
        this.seed = seed;
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
        MatrixUtils.populateElements(resourceMatrix, (x, y) -> Optional.empty());

        // Add all resource types to the world.
        for (final ResourceType type : ResourceType.values()) {
            setGenerationPattern(RESOURCE_PATTERN.get(type));
            MatrixUtils.populateElements(
                resourceMatrix,
                (x, y) -> generateResource(resourceMatrix, type, x, y));
        }
        return resourceMatrix;
    }

    private Optional<IResource> generateResource(
        final Optional<IResource>[][] resourceMatrix,
        final ResourceType type,
        final int x,
        final int y) {
        if (resourceMatrix[y][x].isEmpty()) {
            final float height = sample(x, y);
            if (height >= RESOURCE_THRESHOLD.get(type)) {
                return Optional.of(ResourceFactory.createResource(type, x, y));
            }
        }
        // Keep resource if position is occupied.
        return resourceMatrix[y][x];
    }

}
