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

    @Test
    public void positionEqualsToItSelfIsTrue() {
        // Arrange
        final Position position = new Position(123, 123);
        final boolean isEqual;

        // Act
        isEqual = position.equals(position);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void positionEqualNullIsFalse() {
        // Arrange
        final Position position = new Position(123, 123);
        final boolean isEqual;

        // Act
        isEqual = position.equals(null);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void positionEqualsToIsTrueIfCoordinatesAreSame() {
        // Arrange
        final Position position1 = new Position(123, 123);
        final Position position2 = new Position(123, 123);
        final boolean isEqual;

        // Act
        isEqual = position1.equals(position2);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void positionEqualsIsFalseIfDifferentPositions() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final boolean isEqual;

        // Act
        isEqual = position1.equals(position2);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void positionHashCodeIsSameIfIdenticalPositions() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(1, 1);
        final boolean isEqual;

        // Act
        isEqual = position1.hashCode() == position2.hashCode();

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void positionHashCodeIsDifferentIfDifferentPositions() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final boolean isEqual;

        // Act
        isEqual = position1.hashCode() == position2.hashCode();

        // Assert
        assertThat(isEqual).isFalse();
    }

}
