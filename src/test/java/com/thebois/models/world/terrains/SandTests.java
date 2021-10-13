package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SandTests {

    @Test
    public void getTypeReturnsSandType() {
        // Arrange
        final Sand sand = new Sand(0f, 0f);

        // Act
        final TerrainType type = sand.getType();

        // Assert
        assertThat(type).isEqualTo(TerrainType.SAND);
    }

    @Test
    public void getCostReturns0() {
        // Arrange
        final Sand sand = new Sand(0f, 0f);
        final float expectedCost = 0;
        final float actualCost;
        //
        actualCost = sand.getCost();

        // Assert
        assertThat(actualCost).isEqualTo(expectedCost);
    }

}
