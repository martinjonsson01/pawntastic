package com.thebois.models.world;

import java.util.Random;

import com.thebois.models.world.terrains.AbstractTerrain;
import com.thebois.models.world.terrains.Dirt;
import com.thebois.models.world.terrains.Grass;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.utils.PerlinNoiseGenerator;

public class TerrainGenerator {

    PerlinNoiseGenerator terrainGenerator;

    TerrainGenerator() {
        final Random random = new Random();
        terrainGenerator = new PerlinNoiseGenerator(4, 4, 0.05, 1, random.nextInt(Integer.MAX_VALUE));
    }

    public ITerrain[][] generateTerrainMatrix(final int worldSize) {
        final ITerrain[][] terrainMatrix = new ITerrain[worldSize][worldSize];
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                final float height = terrainGenerator.perlinNoise(x, y);
                if(height > 0.5){
                    terrainMatrix[y][x] = new Dirt(x,y);
                }else{
                    terrainMatrix[y][x] = new Grass(x, y);
                }
            }
        }
        return terrainMatrix;
    }

}
