package com.thebois.models.beings;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PawnTests {

    @Test
    public void arriveAtDestinationTest() {
        // Arrange
        Position startPosition = new Position(0, 0);
        Position endPosition = new Position(2, 2);

        Pawn pawn = new Pawn(startPosition, endPosition);

        // Act
        final int steps = 2;
        for (int i = 0; i < steps; i++) {
            pawn.update();
        }

        // Assert
        assertThat(pawn.getPosition()).isEqualTo(endPosition);
    }

    @Test
    public void arriveAtDestinationNegativeTest() {
        // Arrange
        Position startPosition = new Position(0, 0);
        Position endPosition = new Position(-10, -10);

        Pawn pawn = new Pawn(startPosition, endPosition);

        // Act
        final int steps = 10;
        for (int i = 0; i < steps; i++) {
            pawn.update();
        }

        // Assert
        assertThat(pawn.getPosition()).isEqualTo(endPosition);
    }

}
