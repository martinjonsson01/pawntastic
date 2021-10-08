package com.thebois.models.world.generation;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.world.generation.patterns.IGenerationPattern;
import com.thebois.models.world.generation.patterns.LargeChunks;
import com.thebois.models.world.terrains.Dirt;
import com.thebois.models.world.terrains.Grass;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.models.world.terrains.Sand;
import com.thebois.models.world.terrains.TerrainType;
import com.thebois.utils.MatrixUtils;

/**
 * Generator used to generate tiles for the world.
 */
public class TerrainGenerator extends AbstractGenerator {

    private static final float DIRT_THRESHOLD = 0.5f;
    private static final float SAND_THRESHOLD = 0.35f;
    private static final Map<TerrainType, IGenerationPattern> TERRAIN_PATTERN;
    private static final Map<TerrainType, Float> TERRAIN_THRESHOLD;

    static {
        TERRAIN_PATTERN = new HashMap<>();
        TERRAIN_PATTERN.put(TerrainType.DIRT, new LargeChunks());
        TERRAIN_PATTERN.put(TerrainType.SAND, new LargeChunks());
    }

    static {
        TERRAIN_THRESHOLD = new HashMap<>();
        TERRAIN_THRESHOLD.put(TerrainType.DIRT, DIRT_THRESHOLD);
        TERRAIN_THRESHOLD.put(TerrainType.SAND, SAND_THRESHOLD);
    }

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

        MatrixUtils.populateElements(terrainMatrix, (x, y) -> generateTerrain(x, y));

        return terrainMatrix;
    }

    private ITerrain generateTerrain(
        final int x, final int y) {
        final float height = sample(x, y);
        if (height > DIRT_THRESHOLD) {
            return new Dirt(x, y);
        }
        else if (height > SAND_THRESHOLD) {
            return new Sand(x, y);
        }
        else {
            return new Grass(x, y);
        }
    }

}
