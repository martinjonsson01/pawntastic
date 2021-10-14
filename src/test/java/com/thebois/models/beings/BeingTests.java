package com.thebois.models.beings;

import java.util.Optional;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.inventory.items.Log;
import com.thebois.models.inventory.items.Rock;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.IStructure;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

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

    public static Stream<Arguments> getEqualBeings() {
        final AbstractBeing beingA = createBeing(0, 0, 0, 0, RoleType.BUILDER);
        final AbstractBeing beingB = createBeing(0, 0, 0, 0, RoleType.BUILDER);
        final AbstractBeing beingC = createBeing(0, 0, 0, 0, RoleType.BUILDER);
        return Stream.of(
            Arguments.of(createBeing(0, 0, 0, 0, RoleType.FARMER),
                         createBeing(0, 0, 0, 0, RoleType.FARMER)),
            Arguments.of(createBeing(123, 456, 789, 1011, RoleType.FISHER),
                         createBeing(123, 456, 789, 1011, RoleType.FISHER)),
            /* Reflexiveness test */
            Arguments.of(beingA, beingA),
            /* Symmetry test */
            Arguments.of(beingA, beingB),
            Arguments.of(beingB, beingA),
            /* Transitivity test */
            Arguments.of(beingB, beingC),
            Arguments.of(beingA, beingC));
    }

    private static AbstractBeing createBeing(
        final float startX,
        final float startY,
        final float destinationX,
        final float destinationY,
        final RoleType roleType) {
        final Position currentPosition = new Position(startX, startY);
        final Position destination = new Position(destinationX, destinationY);
        final AbstractRole role = RoleFactory.fromType(roleType);
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        final AbstractBeing being = new Pawn(currentPosition.deepClone(),
                                             destination.deepClone(),
                                             new Random(),
                                             pathFinder,
                                             mockFinder);
        being.setRole(role);
        return being;
    }

    public static Stream<Arguments> getNotEqualBeings() {
        return Stream.of(
            Arguments.of(createBeing(0, 0, 0, 0, RoleType.FARMER),
                         createBeing(0, 0, 0, 0, RoleType.FISHER)),
            Arguments.of(createBeing(0, 0, 0, 0, RoleType.LUMBERJACK),
                         createBeing(1, 0, 0, 0, RoleType.LUMBERJACK)));
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void updateMovesTowardsDestination(
        final Position startPosition, final Position endPosition) {
        // Arrange
        final float distanceToDestination = startPosition.distanceTo(endPosition);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(List.of(endPosition));
        final AbstractBeing being = new Pawn(
            startPosition,
            endPosition,
            mockRandom,
            pathFinder,
            mockFinder);

        // Act
        being.update();

        // Assert
        final Optional<Position> actualDestination = being.getDestination();
        assertThat(actualDestination).isPresent();
        final float distanceAfterUpdate = being.getPosition().distanceTo(actualDestination.get());
        assertThat(distanceAfterUpdate).isLessThanOrEqualTo(distanceToDestination);
    }

    @Test
    public void setRoleWithNullThrowsException() {
        // Arrange
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        final IBeing being = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder,
                                      mockFinder);

        // Assert
        assertThatThrownBy(() -> being.setRole(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void getPathReturnsEqualPositionsToPathFinder() {
        // Arrange
        final Random mockRandom = Mockito.mock(Random.class);
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final Position endPosition = new Position(123, 456);
        final List<Position> actualPath = List.of(endPosition);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(actualPath);
        final AbstractBeing being = new Pawn(
            new Position(),
            endPosition,
            mockRandom,
            pathFinder,
            mockFinder);

        // Act
        final Iterable<Position> pathPositions = being.getPath();

        // Assert
        assertThat(pathPositions).containsExactlyInAnyOrderElementsOf(actualPath);
    }

    @Test
    public void hashCodeReturnsSameIfEqual() {
        // Arrange
        final Position currentPosition = new Position(0, 0);
        final Position destination = new Position(1, 1);
        final AbstractRole role = RoleFactory.farmer();
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        final IBeing first = new Pawn(currentPosition.deepClone(),
                                      destination.deepClone(),
                                      new Random(),
                                      pathFinder,
                                      mockFinder);
        first.setRole(role);
        final IBeing second = new Pawn(currentPosition.deepClone(),
                                       destination.deepClone(),
                                       new Random(),
                                       pathFinder,
                                       mockFinder);
        second.setRole(role);

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isEqualTo(secondHashCode);
    }

    @Test
    public void hashCodeReturnDifferentIfNotEqual() {
        // Arrange
        final AbstractRole role = RoleFactory.farmer();
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        final IBeing first = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder,
                                      mockFinder);
        first.setRole(role);
        final IBeing second = new Pawn(new Position(123, 123),
                                       new Position(983, 1235),
                                       new Random(),
                                       pathFinder,
                                       mockFinder);

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isNotEqualTo(secondHashCode);
    }

    @Test
    public void pathIsRecalculatedAfterStructureIsPlacedInWay() {
        // Arrange
        final Position from = new Position();
        final Position destination = new Position(2, 2);
        final Position obstaclePosition = new Position(1, 1);
        final World world = new World(3);
        final IPathFinder pathFinder = new AstarPathFinder(world);
        final IBeing being = new Pawn(from, destination, new Random(), pathFinder, world);

        // Assert
        final Iterable<Position> oldPath = being.getPath();
        assertThat(oldPath).contains(obstaclePosition);

        // Act
        world.createStructure(obstaclePosition);

        // Assert
        final Iterable<Position> newPath = being.getPath();
        assertThat(newPath).doesNotContain(obstaclePosition);
    }

    @Test
    public void updateDoesNothingWhenPathIsEmpty() {
        // Arrange
        final Position startPosition = new Position();
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(List.of());
        final IBeing being = new Pawn(
            startPosition,
            startPosition,
            new Random(),
            pathFinder,
            mockFinder);

        // Act
        being.update();

        // Assert
        assertThat(being.getPosition()).isEqualTo(startPosition);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
        // Arrange
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IFinder finder = Mockito.mock(IFinder.class);
        final IBeing being = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder,
                                      finder);
        being.setRole(RoleFactory.farmer());

        // Assert
        // noinspection AssertBetweenInconvertibleTypes
        assertThat(being).isNotEqualTo(new Position());
    }

    @ParameterizedTest
    @MethodSource("getEqualBeings")
    public void equalsReturnsTrueForEqualBeings(
        final AbstractBeing first, final AbstractBeing second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualBeings")
    public void equalsReturnsFalseForNotEqualBeings(
        final AbstractBeing first, final AbstractBeing second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void updateDeliversItemsToStructureWhenNearby() {
        // Arrange
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IStructure structure = Mockito.mock(IStructure.class);
        final IFinder finder = Mockito.mock(IFinder.class);

        when(structure.tryDeliverItem(any())).thenReturn(true);

        when(structure.getPosition()).thenReturn(new Position());
        when(finder.findNearestIncompleteStructure(any())).thenReturn(Optional.of(structure));

        final Pawn pawn = new Pawn(new Position(), new Position(), new Random(), pathFinder, finder);

        // Act
        pawn.update();

        // Assert
        verify(structure, atLeastOnce()).tryDeliverItem(any());
    }

    @Test
    public void whenAllStructuresAreCompletePawnPicksRandomDestination() {
        // Arrange
        final Position positionA = new Position(10, 0);
        final Position startPosition = new Position(20, 0);
        final Position positionB = new Position(30, 0);

        final Position correctDestination = new Position(0, 0);

        final World world = new World(50);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);

        world.createStructure(positionA);
        world.createStructure(positionB);

        for (int i = 0; i < 10; i++) {
            for (final IStructure structure : world.getStructures()) {
                structure.tryDeliverItem(new Log());
                structure.tryDeliverItem(new Rock());
            }
        }

        final Pawn pawn = new Pawn(
            startPosition,
            new Position(),
            mockRandom,
            pathFinder,
            world);

        // Act
        pawn.update();

        // Assert
        assertThat(pawn.getFinalDestination()).isEqualTo(correctDestination);
    }

    private static Stream<Arguments> pawnWalkToNearestUnBuiltStructureSource() {
        return Stream.of(
            Arguments.of(
                new Position(5f, 0f),
                new Position(6f, 0f),
                new Position(7f, 0f),
                new Position(8f, 0f)),
            Arguments.of(
                new Position(0f, 2f),
                new Position(0f, 3f),
                new Position(0f, 4f),
                new Position(0f, 5f)),
            Arguments.of(
                new Position(2f, 2f),
                new Position(3f, 3f),
                new Position(4f, 4f),
                new Position(5f, 5f))
        );
    }

    @ParameterizedTest
    @MethodSource("pawnWalkToNearestUnBuiltStructureSource")
    public void pawnWalkToNearestUnBuiltStructure(
        final Position firstDestination, final Position secondDestination, final Position expectedPosition,
        final Position housePosition) {
        // Arrange
        final Stack<Position> path = new Stack<>();
        path.add(expectedPosition);
        path.add(secondDestination);
        path.add(firstDestination);

        final IStructure mockStructure = Mockito.mock(IStructure.class);
        when(mockStructure.getPosition()).thenReturn(housePosition);

        final IPathFinder mockPathFinder = Mockito.mock(IPathFinder.class);
        when(mockPathFinder.path(any(), any())).thenReturn(path);

        final IFinder mockFinder = Mockito.mock(IFinder.class);
        when(mockFinder.findNearestIncompleteStructure(any()))
            .thenReturn(Optional.of(mockStructure));

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   null,
                                   mockPathFinder,
                                   mockFinder);

        // Act
        pawn.update();
        pawn.update();

        // Assert
        assertThat(pawn.getFinalDestination().equals(expectedPosition)).isEqualTo(true);
    }

    @Test
    public void pawnFindsANewStructureWhenNewStructureIsPlaced() {
        // Arrange
        final Position positionA = new Position(10, 0);
        final Position startPosition = new Position(20, 0);
        final Position positionB = new Position(30, 0);
        final Position positionC = new Position(40, 0);

        final Position correctDestination = new Position(39, 0);

        final World world = new World(50);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);

        world.createStructure(positionA);
        world.createStructure(positionB);

        for (int i = 0; i < 10; i++) {
            for (final IStructure structure : world.getStructures()) {
                structure.tryDeliverItem(new Log());
                structure.tryDeliverItem(new Rock());
            }
        }

        final Pawn testPawn = new Pawn(startPosition,
                                       new Position(),
                                       mockRandom,
                                       pathFinder,
                                       world);

        // Act
        testPawn.update();
        testPawn.update();
        world.createStructure(positionC);
        testPawn.update();

        // Assert
        assertThat(testPawn.getFinalDestination()).isEqualTo(correctDestination);
    }

    @Test
    public void pawnDoNotTryToFindNewClosestStructureWhenStillIncomplete() {
        // Arrange
        final IPathFinder mockPathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);

        final IStructure mockStructure = Mockito.mock(IStructure.class);

        final Position expectedPosition = new Position(10f, 20f);

        when(mockFinder.findNearestIncompleteStructure(any()))
            .thenReturn(Optional.of(mockStructure));

        when(mockStructure.getPosition()).thenReturn(expectedPosition);

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   mockPathFinder,
                                   mockFinder);

        // Act
        pawn.update();
        pawn.update();

        // Assert
        final int callsExpected = 1;
        verify(mockFinder, times(callsExpected)).findNearestIncompleteStructure(any());
    }

    @Test
    public void pawnTriesTpFindNewClosestStructureWhenNearestIsCompleted() {
        // Arrange
        final IPathFinder mockPathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);

        final IStructure mockStructureA = Mockito.mock(IStructure.class);
        final IStructure mockStructureB = Mockito.mock(IStructure.class);

        final Position positionA = new Position(10f, 20f);
        final Position positionB = new Position(0f, 5f);

        when(mockFinder.findNearestIncompleteStructure(any()))
            .thenReturn(Optional.of(mockStructureA)).thenReturn(Optional.of(mockStructureB));

        when(mockStructureA.getPosition()).thenReturn(positionA);
        when(mockStructureB.getPosition()).thenReturn(positionB);

        when(mockStructureA.isCompleted()).thenReturn(true);

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   mockPathFinder,
                                   mockFinder);

        // Act
        // Finds A
        pawn.update();
        // Discard A, find B
        pawn.update();

        // Assert
        final int callsExpected = 2;
        verify(mockFinder, times(callsExpected)).findNearestIncompleteStructure(any());
    }

    @Test
    public void pawnLooksForANewPawnWhenDestinationIsNotEqualToNeighborOfClosestStructure() {
        // Arrange
        final IPathFinder mockPathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);

        final IStructure mockStructure = Mockito.mock(IStructure.class);

        final Position structurePosition = new Position(10f, 20f);
        final Position positionA = new Position(0f, 0f);
        final Position positionB = new Position(1f, 1f);
        final Position positionC = new Position(2f, 2f);
        final Position positionD = new Position(3f, 3f);

        when(mockFinder.findNearestIncompleteStructure(any()))
            .thenReturn(Optional.of(mockStructure));

        when(mockPathFinder.path(any(), any())).thenReturn(List.of(
            positionA,
            positionB,
            positionC,
            positionD));

        when(mockStructure.getPosition()).thenReturn(structurePosition);

        when(mockStructure.isCompleted()).thenReturn(false);

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   mockPathFinder,
                                   mockFinder);
        // Act
        // Finds A
        pawn.update();
        // Discard A, find B
        pawn.update();

        // Assert
        final int callsExpected = 3;
        verify(mockPathFinder, times(callsExpected)).path(any(), any());
    }

    @Test
    public void addBeingIncreasesBeingCount() {
        // Arrange
        final Iterable<Position> vacantPositions = List.of(new Position(0, 0));
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final AbstractBeingGroup colony = new Colony(vacantPositions, pathFinder, new World(10));
        final IFinder finder = Mockito.mock(IFinder.class);


        final IBeing being = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder, finder);

        // Act
        final int before = colony.getBeings().size();
        colony.addBeing(being);
        final int after = colony.getBeings().size();

        // Assert
        assertThat(before).isEqualTo(1);
        assertThat(after).isEqualTo(2);
    }

}
