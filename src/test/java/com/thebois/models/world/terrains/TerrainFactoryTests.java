package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TerrainFactoryTests {

    @Test
    public void createTerrainWithGrassEnumReturnsGrassTerrain() {
        // Arrange
        final int x = 0;
        final int y = 0;
        final ITerrain expectedGrass = new Grass(x, y);
        final ITerrain actualGrass;

        // Act
        actualGrass = TerrainFactory.createTerrain(TerrainType.GRASS, x, y);

        // Assert
        assertThat(actualGrass).isEqualTo(expectedGrass);
    }

    @Test
    public void createTerrainWithSandEnumReturnsSandTerrain() {
        // Arrange
        final int x = 0;
        final int y = 0;
        final ITerrain expectedSand = new Sand(x, y);
        final ITerrain actualSand;

        // Act
        actualSand = TerrainFactory.createTerrain(TerrainType.SAND, x, y);

        // Assert
        assertThat(actualSand).isEqualTo(expectedSand);
    }

    @Test
    public void createTerrainWithDirtEnumReturnsDirtTerrain() {
        // Arrange
        final int x = 0;
        final int y = 0;
        final ITerrain expectedDirt = new Dirt(x, y);
        final ITerrain actualDirt;

        // Act
        actualDirt = TerrainFactory.createTerrain(TerrainType.DIRT, x, y);

        // Assert
        assertThat(actualDirt).isEqualTo(expectedDirt);
    }

}
