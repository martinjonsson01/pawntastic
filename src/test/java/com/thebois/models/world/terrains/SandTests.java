package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SandTests {

    @Test
    public void getTypeReturnsSandType() {
        // Arrange
        final ITerrain sand = TerrainFactory.createTerrain(TerrainType.SAND,0,0);

        // Act
        final TerrainType type = sand.getType();

        // Assert
        assertThat(type).isEqualTo(TerrainType.SAND);
    }

    @Test
    public void getCostReturns0() {
        // Arrange
        final ITerrain sand = TerrainFactory.createTerrain(TerrainType.GRASS,0,0);
        final float expectedCost = 0;

        // Act
        final float actualCost = sand.getCost();

        // Assert
        assertThat(actualCost).isEqualTo(expectedCost);
    }

}
