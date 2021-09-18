package com.thebois.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PositionTests {

    @Test
    public void constructorSetsCoordinates() {
        // Arrange
        final float posX = 1.5f;
        final float posY = 2.8f;

        // Act
        final Position position = new Position(posX, posY);

        // Assert
        assertThat(position.getPosX()).isEqualTo(posX);
        assertThat(position.getPosY()).isEqualTo(posY);
    }

    @Test
    public void getSetPositionChangesCoordinates() {
        // Arrange
        final Position position = new Position(0, 0);
        final int posX = 123;
        final int posY = 456;

        // Act
        position.setPosX(posX);
        position.setPosY(posY);

        // Assert
        assertThat(position.getPosX()).isEqualTo(posX);
        assertThat(position.getPosY()).isEqualTo(posY);
    }

}
