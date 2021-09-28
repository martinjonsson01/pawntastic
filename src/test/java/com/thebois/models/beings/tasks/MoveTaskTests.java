package com.thebois.models.beings.tasks;

import java.util.Random;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.Pawn;

import static org.assertj.core.api.Assertions.*;

public class MoveTaskTests {

    @Test
    public void performMovesPosition() {
        // Arrange
        final Position startPosition = new Position();
        final Pawn performer = new Pawn(startPosition, new Position(), new Random());
        final ITask cut = new MoveTask();

        // Act
        cut.perform(performer);

        // Assert
        assertThat(performer.getPosition()).isNotEqualTo(startPosition);
    }

}
