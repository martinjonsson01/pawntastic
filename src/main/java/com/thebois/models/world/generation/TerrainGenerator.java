package com.thebois.models.world.generation;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.world.generation.noises.INoise;
import com.thebois.models.world.generation.noises.NoiseFactory;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.models.world.terrains.TerrainFactory;
import com.thebois.models.world.terrains.TerrainType;
import com.thebois.utils.MatrixUtils;

/**
 * Generator used to generate tiles for the world.
 */
public class TerrainGenerator extends AbstractGenerator {

    private final Map<TerrainType, INoise> terrainNoise;
    private final int seed;
    private final int worldSize;

    /**
     * Instantiate a Terrain Generator with pre-made settings used for generating values.
     *
     * @param worldSize The world size of the world.
     * @param seed      The seed used to generate the world.
     */
    public TerrainGenerator(final int worldSize, final int seed) {
        this.seed = seed;
        this.worldSize = worldSize;
        terrainNoise = new HashMap<>();
        terrainNoise.put(TerrainType.SAND, NoiseFactory.createLargeChunksNoise());
        terrainNoise.put(TerrainType.GRASS, NoiseFactory.createFillMapNoise());
        terrainNoise.put(TerrainType.DIRT, NoiseFactory.createLargeChunksNoise());
    }

    /**
     * Generates a Terrain Matrix with the given world size.
     *
     * @return The terrain matrix.
     */
    public ITerrain[][] generateTerrainMatrix() {
        final ITerrain[][] terrainMatrix = new ITerrain[worldSize][worldSize];

        for (final TerrainType terrainType : TerrainType.values()) {
            setNoise(terrainNoise.get(terrainType));
            final int newSeed = seed + terrainType.getSeedPermutation();
            setSeed(newSeed);
            MatrixUtils.populateElements(terrainMatrix,
                                         (x, y) -> generateTerrain(terrainMatrix,
                                                                   terrainType,
                                                                   x,
                                                                   y));
        }
        return terrainMatrix;
    }

    private ITerrain generateTerrain(
        final ITerrain[][] terrainMatrix, final TerrainType terrainType, final int x, final int y) {
        final float height = sample(x, y);
        if (height >= terrainType.getThreshold()) {
            return TerrainFactory.createTerrain(terrainType, x, y);
        }
        else {
            return terrainMatrix[y][x];
        }
    }

}
