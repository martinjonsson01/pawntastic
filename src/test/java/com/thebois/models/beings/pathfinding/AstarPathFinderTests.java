package com.thebois.models.beings.pathfinding;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.World;

import static org.assertj.core.api.Assertions.*;

public class AstarPathFinderTests {

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(
            Arguments.of(mockPosition(0, 0), mockPosition(0, 0)),
            Arguments.of(mockPosition(10, 10), mockPosition(0, 0)),
            Arguments.of(mockPosition(0, 0), mockPosition(10, 10)),
            Arguments.of(mockPosition(0, 0), mockPosition(11, 23)),
            Arguments.of(mockPosition(29, 3), mockPosition(10, 23)));
    }

    private static Position mockPosition(final int positionX, final int positionY) {
        return new Position(positionX, positionY);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void pathReturnsPositionsThatLeadToDestination(final Position from,
                                                          final Position destination) {
        // Arrange
        final IWorld world = new World(30, 0);
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(from, destination);

        // Assert
        assertThat(path).contains(destination);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void pathReturnsEnoughPositionsToCoverDistance(final Position from,
                                                          final Position destination) {
        // Arrange
        final IWorld world = new World(30, 0);
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(from, destination);

        // Assert
        final int expectedPositions = from.manhattanDistanceTo(destination) + 1;
        assertThat(path.size()).isEqualTo(expectedPositions);
    }

}
