package com.thebois.models.beings;

import com.thebois.models.Position;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PawnTests {

    @Test
    public void walkToPositiveTest() {

        // arrange
        final float startX;
        final float startY;
        final float endX;
        final float endY;

        startX = 0f;
        startY = 0f;

        endX = 10f;
        endY = 10f;

        final Position startPosition = new Position(startX, startY);
        final Position endPosition = new Position(endX, endY);

        final Pawn pawn = new Pawn(startPosition);

        final int steps = 10;

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
        final float startX;
        final float startY;
        final float endX;
        final float endY;

        startX = 0f;
        startY = 0f;

        endX = -10f;
        endY = -10f;

        final Position startPosition = new Position(startX, startY);
        final Position endPosition = new Position(endX, endY);

        final Pawn pawn = new Pawn(startPosition);

        final int steps = 10;
        // Act
        pawn.walkTo(endPosition);
        for (int i = 0; i < steps; i++) {
            pawn.update();
        }

        // Assert
        assertThat(pawn.getPosition()).isEqualTo(endPosition);
    }

}
