package com.thebois.models.beings;

import com.thebois.models.Position;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PawnTests {

    @Test
    public void walkToPositiveTest() {

        // arrange
        int steps = 10;

        float startX, startY, endX, endY;

        startX = 0f;
        startY = 0f;

        endX = 10f;
        endY = 10f;

        Position startPosition = new Position(startX, startY);
        Position endPosition = new Position(endX, endY);

        Pawn pawn = new Pawn(startPosition);

        // Act
        pawn.walkTo(endPosition);
        pawn.walkTo(endPosition);
        for (int i = 0; i < steps; i++) {
            pawn.update();
        }

        // Assert
        assertThat(pawn.getPosition()).isEqualTo(endPosition);
    }

    @Test
    public void walkToNegativeTest() {

        // arrange
        int steps = 10;

        float startX, startY, endX, endY;

        startX = 0f;
        startY = 0f;

        endX = -10f;
        endY = -10f;

        Position startPosition = new Position(startX, startY);
        Position endPosition = new Position(endX, endY);

        Pawn pawn = new Pawn(startPosition);

        // Act
        pawn.walkTo(endPosition);
        for (int i = 0; i < steps; i++) {
            pawn.update();
        }

        // Assert
        assertThat(pawn.getPosition()).isEqualTo(endPosition);
    }

}