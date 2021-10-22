package com.thebois.models.beings.pathfinding;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.TestWorld;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.StructureType;
import com.thebois.models.world.terrains.Grass;
import com.thebois.testutils.MockFactory;

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

    private static Position mockPosition(final int x, final int y) {
        return new Position(x, y);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void pathContainsDestinationLast(final Position start, final Position destination) {
        // Arrange
        final IWorld world = createTestWorld(30);
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(start, destination);

        // Assert
        assertThat(path).last().isEqualTo(destination);
    }

    private World createTestWorld(final int size) {
        return createTestWorld(size, mock(ThreadLocalRandom.class));
    }

    private World createTestWorld(final int size, final ThreadLocalRandom random) {
        return new TestWorld(size, random);
    }

    @Test
    public void pathAvoidsTilesWithHighCost() {
        // Arrange
        final IWorld world = createTestWorld3x3WithObstacles();
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
    private IWorld createTestWorld3x3WithObstacles() {
        final World world = createTestWorld(3);

        world.createStructure(StructureType.HOUSE, 1, 0);
        world.createStructure(StructureType.HOUSE, 1, 1);
        MockFactory.completeAllStructures(world);

        return world;
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
    public void pathReturnsPositionsThatLeadToDestination(
        final Position from, final Position destination) {
        // Arrange
        final IWorld world = createTestWorld(30);
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(from, destination);

        // Assert
        assertThat(path).contains(destination);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void pathReturnsEnoughPositionsToCoverDistance(
        final Position from, final Position destination) {

        // Arrange
        final IWorld world = createTestWorld(30);
        final IPathFinder cut = new AstarPathFinder(world);

        // Act
        final Collection<Position> path = cut.path(from, destination);

        // Assert
        final int expectedPositions = from.manhattanDistanceTo(destination) + 1;
        assertThat(path.size()).isEqualTo(expectedPositions);
    }

}
