package com.thebois.models.beings;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class PawnTests {

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(Arguments.of(new Position(0, 0), new Position(1, 1)),
                         Arguments.of(new Position(0, 0), new Position(99, 99)),
                         Arguments.of(new Position(0, 0), new Position(-1, -1)),
                         Arguments.of(new Position(0, 0), new Position(-99, -99)),
                         Arguments.of(new Position(123, 456), new Position(0, 0)),
                         Arguments.of(new Position(456, 789), new Position(0, 0)));
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void updateMovesPawnTowardsDestination(final Position startPosition,
                                                  final Position endPosition) {
        // Arrange
        final float distanceToDestination = startPosition.distanceTo(endPosition);

        final Pawn pawn = new Pawn(startPosition, endPosition);

        // Act
        pawn.update();

        // Assert
        final float distanceAfterUpdate = pawn.getPosition().distanceTo(pawn.getDestination());
        assertThat(distanceAfterUpdate).isLessThan(distanceToDestination);
    }

}
