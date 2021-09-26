package com.thebois.models.beings.pathfinding;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.world.Grass;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.World;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    /**
     * Mocks a 3x3 world with obstacles.
     * <p>
     * The tiles are laid out like this, with G being grass and X being obstacle:
     * </p>
     * <p>
     * G G G
     * </p>
     * <p>
     * G X G
     * </p>
     * <p>
     * G X G
     * </p>
     *
     * @return The mocked world.
     */
    private IWorld mock3x3WorldWithObstacles() {
        final World world = new World(3, 0);

        world.createStructure(1, 0);
        world.createStructure(1, 1);

        return world;
    }

    private ITile mockObstacle(final int positionX, final int positionY) {
        final ITile mockTile = Mockito.mock(ITile.class);
        when(mockTile.getPosition()).thenReturn(new Position(positionX, positionY));
        when(mockTile.getCost()).thenReturn(100f);
        return mockTile;
    }

    @Test
    public void pathAvoidsTilesWithHighCost() {
        // Arrange
        final IWorld world = mock3x3WorldWithObstacles();
        final IPathFinder cut = new AstarPathFinder(world);
        final Position start = new Position(0, 0);
        final Position destination = new Position(2, 2);
        final Position obstacle1Position = new Position(1, 0);
        final Position obstacle2Position = new Position(1, 1);

        // Act
        final Collection<Position> path = cut.path(start, destination);

        // Assert
        assertThat(path).isNotEmpty();
        assertThat(path).doesNotContain(obstacle1Position, obstacle2Position);
    }

    @Test
    public void pathReturnsEmptyIfNotAbleToReachDestination() {
        // Arrange
        final Position from = new Position();
        final Position destination = new Position(2, 2);
        final IWorld world = Mockito.mock(IWorld.class);
        final ITile fromTile = new Grass(from);
        when(world.getTileAt(from)).thenReturn(fromTile);
        when(world.getTileAt(destination)).thenReturn(new Grass(destination));
        when(world.getNeighboursOf(fromTile)).thenReturn(List.of());
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(from, destination);

        // Assert
        assertThat(path).isEmpty();
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
