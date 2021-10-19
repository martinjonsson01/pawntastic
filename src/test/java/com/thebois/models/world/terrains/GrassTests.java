package com.thebois.models.world.terrains;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class GrassTests {

    @Test
    public void getTypeReturnsGrassType() {
        // Arrange
        final Grass grass = new Grass(0f, 0f);

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
        final Grass grass = new Grass(x, y);

        // Act
        final Position actualPosition = grass.getPosition();

        // Assert
        assertThat(actualPosition.getX()).isEqualTo(x);
        assertThat(actualPosition.getY()).isEqualTo(y);
    }

    @Test
    public void getCostReturnsGreaterThanOrEqualToZero() {
        // Arrange
        final Grass grass = new Grass(0f, 0f);

        // Act
        final float cost = grass.getCost();

        // Assert
        assertThat(cost).isGreaterThanOrEqualTo(0f);
    }

}
