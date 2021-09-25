package com.thebois.models.world.terrains;

import java.util.Random;

import com.thebois.utils.PerlinNoiseGenerator;

/**
 * Generator used to generate tiles for the world.
 */
public class TerrainGenerator {

    private static final float DIRT_THRESHOLD = 0.5f;
    private final PerlinNoiseGenerator terrainGenerator;

    /**
     * Instantiate a Terrain Generator with pre-made settings used for generating values.
     */
    public TerrainGenerator() {
        final Random random = new Random();
        final int octaves = 4;
        final double amplitude = 4;
        final double frequency = 0.05;
        final double persistence = 1.0;
        final int seed = random.nextInt(Integer.MAX_VALUE);
        terrainGenerator = new PerlinNoiseGenerator(octaves,
                                                    amplitude,
                                                    frequency,
                                                    persistence,
                                                    seed);
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
                final float height = terrainGenerator.perlinNoise(x, y);
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
