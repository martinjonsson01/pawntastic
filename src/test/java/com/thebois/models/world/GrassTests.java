package com.thebois.models.world;

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
        final int posX = 123;
        final int posY = 456;
        final Grass grass = new Grass(posX, posY);

        // Act
        final Position actualPosition = grass.getPosition();

        // Assert
        assertThat(actualPosition.getPosX()).isEqualTo(posX);
        assertThat(actualPosition.getPosY()).isEqualTo(posY);
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
