package com.thebois.models.world.generation;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.world.generation.patterns.FillMap;
import com.thebois.models.world.generation.patterns.IGenerationPattern;
import com.thebois.models.world.generation.patterns.LargeChunks;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.models.world.terrains.TerrainFactory;
import com.thebois.models.world.terrains.TerrainType;
import com.thebois.utils.MatrixUtils;

/**
 * Generator used to generate tiles for the world.
 */
public class TerrainGenerator extends AbstractGenerator {

    private static final float DIRT_THRESHOLD = 0.5f;
    private static final float SAND_THRESHOLD = 0.35f;
    private static final float GRASS_THRESHOLD = 0f;
    private static final Map<TerrainType, IGenerationPattern> TERRAIN_PATTERN;
    private static final Map<TerrainType, Float> TERRAIN_THRESHOLD;

    static {
        TERRAIN_PATTERN = new HashMap<>();
        TERRAIN_PATTERN.put(TerrainType.DIRT, new LargeChunks());
        TERRAIN_PATTERN.put(TerrainType.SAND, new LargeChunks());
        TERRAIN_PATTERN.put(TerrainType.GRASS, new FillMap());
    }

    static {
        TERRAIN_THRESHOLD = new HashMap<>();
        TERRAIN_THRESHOLD.put(TerrainType.DIRT, DIRT_THRESHOLD);
        TERRAIN_THRESHOLD.put(TerrainType.SAND, SAND_THRESHOLD);
        TERRAIN_THRESHOLD.put(TerrainType.GRASS, GRASS_THRESHOLD);
    }

    private static final int DIRT_SEED_OFFSET = 351;
    private final Map<TerrainType, Integer> terrainSeed;

    /**
     * Instantiate a Terrain Generator with pre-made settings used for generating values.
     *
     * @param seed The seed used to generate the world.
     */
    public TerrainGenerator(final int seed) {
        super(new LargeChunks(), seed);
        terrainSeed = new HashMap<>();
        terrainSeed.put(TerrainType.SAND, seed);
        terrainSeed.put(TerrainType.GRASS, seed);
        terrainSeed.put(TerrainType.DIRT, seed + DIRT_SEED_OFFSET);
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

        for (final TerrainType terrainType : TerrainType.values()) {
            setGenerationPattern(TERRAIN_PATTERN.get(terrainType));
            setSeed(terrainSeed.get(terrainType));
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
        if (height >= TERRAIN_THRESHOLD.get(terrainType)) {
            return TerrainFactory.createTerrain(terrainType, x, y);
        }
        else {
            return terrainMatrix[y][x];
        }
    }

}
