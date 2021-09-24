package com.thebois.models.world;

import com.thebois.models.world.terrains.AbstractTerrain;
import com.thebois.models.world.terrains.Dirt;
import com.thebois.models.world.terrains.Grass;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.utils.PerlinNoiseGenerator;

public class TerrainGenerator {

    PerlinNoiseGenerator terrainGenerator;

    TerrainGenerator() {
        terrainGenerator = new PerlinNoiseGenerator(1, 1, 1, 1, 1);
    }

    public ITerrain[][] generateTerrainMatrix(final int worldSize) {
        final ITerrain[][] terrainMatrix = new ITerrain[worldSize][worldSize];
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                float height = terrainGenerator.perlinNoise(x,y);
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
