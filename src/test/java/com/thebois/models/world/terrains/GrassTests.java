package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class GrassTests {

    @Test
    public void getTypeReturnsGrassType() {
        // Arrange
        final ITerrain grass = TerrainFactory.createTerrain(TerrainType.GRASS, 0, 0);

        // Act
        final TerrainType type = grass.getType();

        // Assert
        assertThat(type).isEqualTo(TerrainType.GRASS);
    }

    @Test
    public void getPositionReturnsCorrectCoordinates() {
        // Arrange
        final int x = 123;
        final int y = 456;
        final ITerrain grass = TerrainFactory.createTerrain(TerrainType.GRASS, x, y);

        // Act
        final Position actualPosition = grass.getPosition();

        // Assert
        assertThat(actualPosition.getX()).isEqualTo(x);
        assertThat(actualPosition.getY()).isEqualTo(y);
    }

    @Test
    public void getCostReturnsGreaterThanOrEqualToZero() {
        // Arrange
        final ITerrain grass = TerrainFactory.createTerrain(TerrainType.GRASS, 0, 0);

        // Act
        final float cost = grass.getCost();

        // Assert
        assertThat(cost).isGreaterThanOrEqualTo(0f);
    }

}
