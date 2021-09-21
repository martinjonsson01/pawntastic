package com.thebois.models.beings;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class PawnTests {

    @Test
    public void arriveAtDestinationTest() {
        // Arrange
        final Position startPosition = new Position(0, 0);
        final Position endPosition = new Position(3, 4);

        final Pawn pawn = new Pawn(startPosition, endPosition);

        // Act
        final int steps = 5;
        for (int i = 0; i < steps; i++) {
            pawn.update();
        }

        // Assert
        assertThat(pawn.getPosition()).isEqualTo(endPosition);
    }

    @Test
    public void arriveAtDestinationNegativeTest() {
        // Arrange
        final Position startPosition = new Position(0, 0);
        final Position endPosition = new Position(3, -4);

        final Pawn pawn = new Pawn(startPosition, endPosition);

        // Act
        final int steps = 5;
        for (int i = 0; i < steps; i++) {
            pawn.update();
        }

        // Assert
        assertThat(pawn.getPosition()).isEqualTo(endPosition);
    }

}
