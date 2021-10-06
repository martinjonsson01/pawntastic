package com.thebois.models.world.generation;

import com.thebois.models.world.generation.patterns.LargeChunks;
import com.thebois.models.world.terrains.Dirt;
import com.thebois.models.world.terrains.Grass;
import com.thebois.models.world.terrains.ITerrain;

/**
 * Generator used to generate tiles for the world.
 */
public class TerrainGenerator extends AbstractGenerator {

    private static final float DIRT_THRESHOLD = 0.5f;

    /**
     * Instantiate a Terrain Generator with pre-made settings used for generating values.
     *
     * @param seed The seed used to generate the world.
     */
    public TerrainGenerator(final int seed) {
        super(new LargeChunks(), seed);
    }

    /**
     * Generates a Terrain Matrix with the given world size.
     *
     * @param worldSize The size of the world.
     *
     * @return The terrain matrix.
     */
    public ITerrain[][] generateTerrainMatrix(final int worldSize) {
        final ITerrain[][] terrainMatrix = new ITerrain[worldSize][worldSize];
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                final float height = sample(x, y);
                if (height > DIRT_THRESHOLD) {
                    terrainMatrix[y][x] = new Dirt(x, y);
                }
                else {
                    terrainMatrix[y][x] = new Grass(x, y);
                }
            }
        }
        return terrainMatrix;
    }

}
