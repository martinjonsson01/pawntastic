package com.thebois.models;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class PositionTests {

    public static Stream<Arguments> getPositionsAndDistances() {
        return Stream.of(Arguments.of(new Position(0, 0), new Position(0, 0), 0f),
                         Arguments.of(new Position(0, 0), new Position(1, 1), 1.41421356f),
                         Arguments.of(new Position(-123, -456), new Position(123, 456), 944.5952f),
                         Arguments.of(new Position(1, 0), new Position(2, 0), 1f),
                         Arguments.of(new Position(0, 1), new Position(0, 2), 1f),
                         Arguments.of(new Position(-20, -30), new Position(0, 0), 36.0555f));
    }

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

    @ParameterizedTest
    @MethodSource("getPositionsAndDistances")
    public void distanceToReturnsCorrectDistance(final Position first,
                                                 final Position second,
                                                 final float expectedDistance) {
        // Act
        final float actualDistance = first.distanceTo(second);

        // Assert
        assertThat(actualDistance).isCloseTo(expectedDistance, Assertions.offset(0.001f));
    }

}
