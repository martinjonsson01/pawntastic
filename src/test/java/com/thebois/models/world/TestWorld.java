package com.thebois.models.world;

import java.util.Random;

import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.terrains.Grass;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.utils.MatrixUtils;

public class TestWorld extends World {

    /**
     * Initiates the world with the given size, filled with grass and no resources.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     * @param random    The source of random numbers.
     */
    public TestWorld(final int worldSize, final Random random) {
        super(worldSize, 0, random);
    }

    @Override
    protected ITerrain[][] setUpTerrain(final int worldSize, final int seed) {
        final ITerrain[][] terrainMatrix = new ITerrain[worldSize][worldSize];
        MatrixUtils.populateElements(terrainMatrix, Grass::new);
        return terrainMatrix;
    }

    @Override
    protected IResource[][] setUpResources(final int worldSize, final int seed) {
        final IResource[][] resourceMatrix = new IResource[worldSize][worldSize];
        MatrixUtils.populateElements(resourceMatrix, (x, y) -> null);
        return resourceMatrix;
    }

}
