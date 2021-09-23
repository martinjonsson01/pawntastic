package com.thebois.models.beings.pathfinding;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.world.Grass;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.World;

import static org.assertj.core.api.Assertions.*;

public class AstarPathFinderTests {

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(
            Arguments.of(mockTile(0, 0), mockTile(0, 0)),
            Arguments.of(mockTile(10, 10), mockTile(0, 0)),
            Arguments.of(mockTile(0, 0), mockTile(10, 10)),
            Arguments.of(mockTile(0, 0), mockTile(11, 23)),
            Arguments.of(mockTile(30, 3), mockTile(10, 23)));
    }

    private static ITile mockTile(final int positionX, final int positionY) {
        return new Grass(positionX, positionY);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void pathReturnsPositionsThatLeadToDestination(final ITile from,
                                                          final ITile destination) {
        // Arrange
        final IWorld world = new World(30, 0);
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(from, destination);

        // Assert
        assertThat(path).contains(destination.getPosition());
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void pathReturnsEnoughPositionsToCoverDistance(final ITile from,
                                                          final ITile destination) {
        // Arrange
        final IWorld world = new World(30, 0);
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(from, destination);

        // Assert
        final int expectedPositions =
            from.getPosition().manhattanDistanceTo(destination.getPosition()) + 1;
        assertThat(path.size()).isEqualTo(expectedPositions);
    }

}
