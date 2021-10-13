package com.thebois.models.beings;

import java.util.Optional;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.IStructure;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(Arguments.of(new Position(0, 0), new Position(0, 0)),
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
        final AbstractBeing being = new Pawn(currentPosition.deepClone(),
                                             destination.deepClone(),
                                             new Random(),
                                             pathFinder,
                                             new World(10));
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
        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(List.of(endPosition));
        final AbstractBeing being = new Pawn(
            startPosition,
            endPosition,
            mockRandom,
            pathFinder,
            new World(10));

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
        final IBeing being = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder,
                                      new World(10));

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
        when(pathFinder.path(any(), any())).thenReturn(actualPath);
        final AbstractBeing being = new Pawn(
            new Position(),
            endPosition,
            mockRandom,
            pathFinder,
            new World(10));

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
        final IBeing first = new Pawn(currentPosition.deepClone(),
                                      destination.deepClone(),
                                      new Random(),
                                      pathFinder,
                                      new World(10));
        first.setRole(role);
        final IBeing second = new Pawn(currentPosition.deepClone(),
                                       destination.deepClone(),
                                       new Random(),
                                       pathFinder,
                                       new World(10));
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
        final IBeing first = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder,
                                      new World(10));
        first.setRole(role);
        final IBeing second = new Pawn(new Position(123, 123),
                                       new Position(983, 1235),
                                       new Random(),
                                       pathFinder,
                                       new World(10));

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
        when(pathFinder.path(any(), any())).thenReturn(List.of());
        final IBeing being = new Pawn(
            startPosition,
            startPosition,
            new Random(),
            pathFinder,
            new World(10));

        // Act
        being.update();

        // Assert
        assertThat(being.getPosition()).isEqualTo(startPosition);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
        // Arrange
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IBeing being = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder,
                                      new World(10));
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
    public void doesPawnDeliverItem() {
        // Arrange
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final World world = Mockito.mock(World.class);
        final IStructure structure = Mockito.mock(IStructure.class);

        when(structure.deliverItem(any())).thenReturn(true);

        when(structure.getPosition()).thenReturn(new Position());
        when(world.findNearestIncompleteStructure(any())).thenReturn(Optional.of(structure));

        final Pawn pawn = new Pawn(new Position(), new Position(), new Random(), pathFinder, world);

        // Act
        pawn.update();

        // Assert
        verify(structure, atLeastOnce()).deliverItem(any());

    }

    @Test
    public void doesPawnWalkToNearestUnBuiltBuilding() {
        // Arrange
        final Position positionA = new Position(10, 0);
        final Position positionB = new Position(30, 0);
        final Position startPosition = new Position(20, 0);

        final Position correctDestination = new Position(11, 1);

        final World world = new World(50);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        world.createStructure(positionA);
        world.createStructure(positionB);

//        for (int i = 0; i < 10; i++) {
//            world.getStructureAt(positionB).get().deliverItem(new Rock());
//            world.getStructureAt(positionB).get().deliverItem(new Log());
//
//        }

        final Pawn testPawn = new Pawn(
            startPosition,
            new Position(),
            new Random(),
            pathFinder,
            world);

        // Act
        testPawn.update();

        // Assert
        assertThat(testPawn.getFinalDestination().get()).isEqualTo(correctDestination);
    }

    @Test
    public void allStructuresCompleted() {
        // Arrange
        final Position positionA = new Position(10, 0);
        final Position startPosition = new Position(20, 0);
        final Position positionB = new Position(30, 0);

        final Position correctDestination = new Position(0, 0);

        final World world = new World(50);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);

        world.createStructure(positionA);
        world.createStructure(positionB);

//        for (int i = 0; i < 10; i++) {
//            world.getStructureAt(positionB).get().deliverItem(new Rock());
//            world.getStructureAt(positionB).get().deliverItem(new Log());
//            world.getStructureAt(positionA).get().deliverItem(new Rock());
//            world.getStructureAt(positionA).get().deliverItem(new Log());
//
//        }

        final Pawn testPawn = new Pawn(
            startPosition,
            new Position(),
            mockRandom,
            pathFinder,
            world);

        // Act
        testPawn.update();

        // Assert
        assertThat(testPawn.getFinalDestination().get()).isEqualTo(correctDestination);
    }

    @Test
    public void allStructuresCompletedNewPlaced() {
        // Arrange
        final Position positionA = new Position(10, 0);
        final Position startPosition = new Position(20, 0);
        final Position positionB = new Position(30, 0);
        final Position positionC = new Position(40, 0);

        final Position correctDestination = new Position(41, 1);

        final World world = new World(50);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);

        world.createStructure(positionA);
        world.createStructure(positionB);

//        for (int i = 0; i < 10; i++) {
//            world.getStructureAt(positionB).get().deliverItem(new Rock());
//            world.getStructureAt(positionB).get().deliverItem(new Log());
//            world.getStructureAt(positionA).get().deliverItem(new Rock());
//            world.getStructureAt(positionA).get().deliverItem(new Log());
//        }

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
        assertThat(testPawn.getFinalDestination().get()).isEqualTo(correctDestination);
    }

    @Test
    public void deliverItemsToCloseStructure() {
        // Arrange
        final Position positionA = new Position(10, 0);
        final Position startPosition = new Position(11, 0);

        final World world = new World(50);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);

        world.createStructure(positionA);

//        final IStructure structureAtA = world.getStructureAt(positionA).get();

        final Pawn testPawn = new Pawn(startPosition,
                                       new Position(),
                                       mockRandom,
                                       pathFinder,
                                       world);

        // Act
//        final float initialBuiltRatio = structureAtA.getBuiltRatio();
//        testPawn.update();
//
//        // Assert
//        assertThat(initialBuiltRatio).isLessThan(structureAtA.getBuiltRatio());
    }

    @Test
    public void findNewUnBuiltStructure() {
        // Arrange
        final Position positionA = new Position(10, 0);
        final Position positionB = new Position(20, 0);
        final Position startPosition = new Position(11, 0);

        final World world = new World(50);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);

        world.createStructure(positionA);
        world.createStructure(positionB);

//        final IStructure structureAtA = world.getStructureAt(positionA).get();
//        final IStructure structureAtB = world.getStructureAt(positionB).get();

//        for (int i = 0; i < 9; i++) {
//            structureAtA.deliverItem(new Log());
//            structureAtA.deliverItem(new Rock());
//
//            structureAtB.deliverItem(new Log());
//            structureAtB.deliverItem(new Rock());
//        }

        final Pawn testPawn = new Pawn(startPosition,
                                       new Position(),
                                       mockRandom,
                                       pathFinder,
                                       world);

        // Act
//        final float initialBuiltRatio = structureAtA.getBuiltRatio();
        // deliver items to structureAtA
        testPawn.update();
        // update to find new un-built structure
        testPawn.update();

        testPawn.getFinalDestination();

        // Assert
//        assertThat(initialBuiltRatio).isLessThan(structureAtA.getBuiltRatio());
    }

    @Test
    public void addBeingIncreasesBeingCount() {
        // Arrange
        final Iterable<Position> vacantPositions = List.of(new Position(0, 0));
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final AbstractBeingGroup colony = new Colony(vacantPositions, pathFinder, new World(10));

        final IBeing being = new Pawn(new Position(0, 0),
                                      new Position(1, 1),
                                      new Random(),
                                      pathFinder, new World(10));

        // Act
        final int before = colony.getBeings().size();
        colony.addBeing(being);
        final int after = colony.getBeings().size();

        // Assert
        assertThat(before).isEqualTo(1);
        assertThat(after).isEqualTo(2);
    }

}
