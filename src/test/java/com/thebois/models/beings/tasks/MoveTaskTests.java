package com.thebois.models.beings.tasks;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.pathfinding.IPathFinder;

import static org.mockito.Mockito.*;

public class MoveTaskTests {

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(
            Arguments.of(new Position(0, 0), new Position(0, 0)),
            Arguments.of(new Position(0, 0), new Position(1, 1)),
            Arguments.of(new Position(0, 0), new Position(1, 0)),
            Arguments.of(new Position(0, 0), new Position(99, 99)),
            Arguments.of(new Position(0, 0), new Position(-1, -1)),
            Arguments.of(new Position(0, 0), new Position(-99, -99)),
            Arguments.of(new Position(123, 456), new Position(0, 0)),
            Arguments.of(new Position(456, 789), new Position(0, 0)));
    }

    @Test
    public void performMovesPosition() {
        // Arrange
        final Position destination = new Position(10, 10);
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        final IPathFinder pathFinder = mock(IPathFinder.class);
        final ITask cut = new MoveTask(destination, pathFinder);

        // Act
        cut.perform(performer);

        // Assert
        verify(performer).setDestination(destination);
    }

    @Test
    public void pathIsRecalculatedAfterStructureIsPlacedInWay() {
        /* // Arrange
        final Position from = new Position();
        final Position obstaclePosition = new Position(1, 1);
        final World world = new World(3, 0);
        final IBeing being = new Pawn(from);

        // Assert
        final Iterable<Position> oldPath = being.getPath();
        assertThat(oldPath).contains(obstaclePosition);

        // Act
        world.createStructure(obstaclePosition);

        // Assert
        final Iterable<Position> newPath = being.getPath();
        assertThat(newPath).doesNotContain(obstaclePosition);*/
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void updateMovesTowardsDestination(
        final Position startPosition, final Position endPosition) {
        /* // Arrange
        final float distanceToDestination = startPosition.distanceTo(endPosition);
        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(List.of(endPosition));
        final AbstractBeing being = new Pawn(startPosition);

        // Act
        being.update();

        // Assert
        final Optional<Position> actualDestination = being.getDestination();
        assertThat(actualDestination).isPresent();
        final float distanceAfterUpdate = being.getPosition().distanceTo(actualDestination.get());
        assertThat(distanceAfterUpdate).isLessThanOrEqualTo(distanceToDestination);*/
    }

}
