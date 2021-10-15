package com.thebois.models.world.generation;

import com.thebois.models.world.terrains.ITerrain;
import com.thebois.models.world.terrains.TerrainFactory;
import com.thebois.models.world.terrains.TerrainType;
import com.thebois.utils.MatrixUtils;

/**
 * Generator that uses noise to generate terrain.
 */
public class TerrainGenerator extends AbstractGenerator {

    /**
     * Instantiate a Terrain Generator with pre-made settings used for generating terrain.
     *
     * @param worldSize The world size of the world.
     * @param seed      The seed used to generate the world.
     */
    public TerrainGenerator(final int worldSize, final int seed) {
        super(worldSize, seed);
    }

    /**
     * Generates a Terrain Matrix with the given world size.
     *
     * @return The terrain matrix.
     */
    public ITerrain[][] generateTerrainMatrix() {
        final ITerrain[][] terrainMatrix = new ITerrain[getWorldSize()][getWorldSize()];

        for (final TerrainType terrainType : TerrainType.values()) {
            setNoise(terrainType.getNoise());
            final int newSeed = getSeed() + terrainType.getSeedPermutation();
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
