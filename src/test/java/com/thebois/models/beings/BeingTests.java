package com.thebois.models.beings;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionSource;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.structures.IStructure;
import com.thebois.testutils.InMemorySerialize;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(Arguments.of(new Position(0, 0), new Position(0, 0)),
                         Arguments.of(new Position(0, 0), new Position(1, 1)),
                         Arguments.of(new Position(0, 0), new Position(1, 0)),
                         Arguments.of(new Position(0, 0), new Position(0, 1)),
                         Arguments.of(new Position(0, 0), new Position(99, 99)),
                         Arguments.of(new Position(0, 0), new Position(-1, -1)),
                         Arguments.of(new Position(0, 0), new Position(-99, -99)),
                         Arguments.of(new Position(123, 456), new Position(0, 0)),
                         Arguments.of(new Position(456, 789), new Position(0, 0)));
    }

    public static Stream<Arguments> getEqualBeings() {
        mockFactoryDependencies();
        final AbstractBeing beingA = createBeing(0, 0, RoleType.BUILDER);
        final AbstractBeing beingB = createBeing(0, 0, RoleType.BUILDER);
        final AbstractBeing beingC = createBeing(0, 0, RoleType.BUILDER);
        return Stream.of(Arguments.of(createBeing(), createBeing()),
                         Arguments.of(createBeing(0, 0, RoleType.FARMER),
                                      createBeing(0, 0, RoleType.FARMER)),
                         Arguments.of(createBeing(123, 456, RoleType.FISHER),
                                      createBeing(123, 456, RoleType.FISHER)),
                         Arguments.of(beingA, beingA),
                         Arguments.of(beingA, beingB),
                         Arguments.of(beingB, beingA),
                         Arguments.of(beingB, beingC),
                         Arguments.of(beingA, beingC));
    }

    private static void mockFactoryDependencies() {
        ActionFactory.setPathFinder(mock(IPathFinder.class));
        RoleFactory.setWorld(mock(IWorld.class));
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        RoleFactory.setStructureFinder(mock(IStructureFinder.class));
    }

    private static AbstractBeing createBeing(
        final float startX, final float startY, final RoleType roleType) {
        final AbstractRole role = RoleFactory.fromType(roleType);
        return createBeing(new Position(startX, startY), role);
    }

    private static AbstractBeing createBeing() {
        return createBeing(mock(IInventory.class));
    }

    private static AbstractBeing createBeing(
        final Position currentPosition, final AbstractRole role) {
        return createBeing(currentPosition, mock(IInventory.class), role);
    }

    private static AbstractBeing createBeing(final IInventory inventory) {
        final AbstractRole role = RoleFactory.idle();
        return createBeing(new Position(), inventory, role);
    }

    private static AbstractBeing createBeing(
        final Position currentPosition, final IInventory inventory, final AbstractRole role) {
        return new Pawn(currentPosition.deepClone(), inventory, role);
    }

    public static Stream<Arguments> getNotEqualBeings() {
        mockFactoryDependencies();
        return Stream.of(Arguments.of(createBeing(0, 0, RoleType.FARMER),
                                      createBeing(0, 0, RoleType.FISHER)),
                         Arguments.of(createBeing(0, 0, RoleType.LUMBERJACK),
                                      createBeing(1, 0, RoleType.LUMBERJACK)));
    }

    public static Stream<Arguments> getDestinations() {
        return Stream.of(Arguments.of(new Position(0, 1)),
                         Arguments.of(new Position(1, 0)),
                         Arguments.of(new Position(99, 99)),
                         Arguments.of(new Position(-1, -1)),
                         Arguments.of(new Position(-99, -99)));
    }

    private static Stream<Arguments> getStartingPositionFinalExpectedPositionAndHousePosition() {
        return Stream.of(Arguments.of(new Position(6f, 0f),
                                      new Position(7f, 0f),
                                      new Position(8f, 0f)),
                         Arguments.of(new Position(0f, 3f),
                                      new Position(0f, 4f),
                                      new Position(0f, 5f)),
                         Arguments.of(new Position(3f, 3f),
                                      new Position(4f, 4f),
                                      new Position(5f, 5f)));
    }

    @BeforeEach
    public void setup() {
        mockFactoryDependencies();
    }

    @AfterEach
    public void teardown() {
        ActionFactory.setPathFinder(null);
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        RoleFactory.setStructureFinder(null);
    }

    @ParameterizedTest
    @MethodSource("getDestinations")
    public void updateMovesTowardsDestinationWhenNotAtDestination(final Position destination) {
        // Arrange
        final Position start = new Position();
        final AbstractRole nothingRole = new NothingRole();
        final AbstractBeing being = createBeing(start, nothingRole);
        being.setDestination(destination);
        final float distanceToDestinationBefore = start.distanceTo(being.getDestination());

        // Act
        being.update(0.1f);

        // Assert
        final Position actualPosition = being.getPosition();
        final float distanceToDestinationAfter = actualPosition.distanceTo(being.getDestination());
        assertThat(distanceToDestinationAfter).isLessThan(distanceToDestinationBefore);
    }

    @Test
    public void addItemCallsInjectedInventory() {
        // Arrange
        final IInventory inventory = mock(IInventory.class);
        final IActionPerformer being = createBeing(inventory);
        final IItem item = mock(IItem.class);

        // Act
        being.addItem(item);

        // Assert
        verify(inventory, times(1)).add(item);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void updateDoesNotOvershootDestinationWhenDeltaTimeIsLarge(
        final Position startPosition, final Position destination) {
        // Arrange
        final Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        final IPathFinder pathFinder = mock(IPathFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(List.of(destination));
        final AbstractBeing being = new Pawn(startPosition,
                                             destination,
                                             mockRandom,
                                             pathFinder,
                                             mock(IStructureFinder.class));

        final float distanceBefore = startPosition.distanceTo(destination);

        // Act
        being.update(10f);

        // Assert
        final Position newPosition = being.getPosition();
        final float distanceAfter = newPosition.distanceTo(destination);
        assertThat(distanceAfter).isLessThanOrEqualTo(distanceBefore);
    }

    @Test
    public void updateDoesNotMoveTowardsDestinationWhenDeltaTimeIsZero() {
        // Arrange
        final Position startPosition = new Position(0, 0);
        final Position destination = new Position(10, 0);
        final Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        final IPathFinder pathFinder = mock(IPathFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(List.of(destination));
        final AbstractBeing being = new Pawn(startPosition,
                                             destination,
                                             mockRandom,
                                             pathFinder,
                                             mock(IStructureFinder.class));

        // Act
        being.update(0f);

        // Assert
        final Position newPosition = being.getPosition();
        assertThat(newPosition).isEqualTo(startPosition);
    }

    @Test
    public void updateMovesFurtherTowardsDestinationWhenDeltaTimeIsGreater() {
        // Arrange
        final Position destination = new Position(10, 0);
        final Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(0);
        final IPathFinder pathFinder = mock(IPathFinder.class);
        when(pathFinder.path(any(), any())).thenReturn(List.of(destination));
        final AbstractBeing slowerBeing = new Pawn(new Position(0, 0),
                                                   destination,
                                                   mockRandom,
                                                   pathFinder,
                                                   mock(IStructureFinder.class));
        final AbstractBeing fasterBeing = new Pawn(new Position(0, 0),
                                                   destination,
                                                   mockRandom,
                                                   pathFinder,
                                                   mock(IStructureFinder.class));

        // Act
        slowerBeing.update(0.01f);
        fasterBeing.update(0.1f);

        // Assert
        final float slowerDistanceAfterUpdate = slowerBeing.getPosition().distanceTo(destination);
        final float fasterDistanceAfterUpdate = fasterBeing.getPosition().distanceTo(destination);
        assertThat(slowerDistanceAfterUpdate).isGreaterThan(fasterDistanceAfterUpdate);
    }

    @Test
    public void setRoleWithNullThrowsException() {
        // Arrange
        final IBeing being = createBeing();

        // Assert
        assertThatThrownBy(() -> being.setRole(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateGetsTaskFromRole() {
        // Arrange
        final AbstractRole role = mock(AbstractRole.class);
        final AbstractBeing being = createBeing();
        final IAction task = mock(IAction.class);
        being.setRole(role);
        when(role.obtainNextAction(being)).thenReturn(task);

        // Act
        being.update(0.1f);

        // Assert
        verify(role, times(1)).obtainNextAction(being);
    }

    @Test
    public void updatePerformsTask() {
        // Arrange
        final AbstractRole role = mock(AbstractRole.class);
        final AbstractBeing being = createBeing();
        final IAction task = mock(IAction.class);
        being.setRole(role);
        when(role.obtainNextAction(being)).thenReturn(task);

        // Act
        being.update(0.1f);

        // Assert
        verify(task, times(1)).perform(being);
    }

    @Test
    public void hashCodeReturnsSameIfEqual() {
        // Arrange
        final AbstractRole role = RoleFactory.farmer();
        final IBeing first = createBeing();
        first.setRole(role);
        final IBeing second = createBeing();
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
        final IBeing first = createBeing(new Position(0, 0), RoleFactory.farmer());
        final IBeing second = createBeing(new Position(123, 123), RoleFactory.idle());

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isNotEqualTo(secondHashCode);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
        // Arrange
        final IBeing being = createBeing();
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
        final IPathFinder pathFinder = mock(IPathFinder.class);
        final IStructure structure = mock(IStructure.class);
        final IStructureFinder finder = mock(IStructureFinder.class);

        when(structure.tryDeliverItem(any())).thenReturn(true);

        when(structure.getPosition()).thenReturn(new Position());
        when(finder.getNearbyIncompleteStructure(any())).thenReturn(Optional.of(structure));

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   pathFinder,
                                   finder);

        // Act
        pawn.update(0.1f);

        // Assert
        verify(structure, atLeastOnce()).tryDeliverItem(any());
    }

    @Test
    public void whenAllStructuresAreCompletePawnPicksRandomDestination() {
        // Arrange
        final IPathFinder mockPathFinder = mock(IPathFinder.class);
        final IStructureFinder mockFinder = mock(IStructureFinder.class);

        final Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(5);

        final Position positionA = new Position(10f, 20f);
        final IStructure structureA = mock(IStructure.class);
        when(structureA.getPosition()).thenReturn(positionA);
        when(structureA.isCompleted()).thenReturn(true);

        final Position positionB = new Position(0f, 12f);
        final IStructure structureB = mock(IStructure.class);
        when(structureB.getPosition()).thenReturn(positionB);
        when(structureB.isCompleted()).thenReturn(true);

        when(mockFinder.getNearbyIncompleteStructure(any()))
            .thenReturn(Optional.of(structureA))
            .thenReturn(Optional.of(structureB));

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   mockRandom,
                                   mockPathFinder,
                                   mockFinder);

        // Act
        // Finds an incomplete structure.
        pawn.update(0.1f);
        // Does pick a new incomplete structure because current structure is completed

        // Assert
        verify(mockRandom, times(2)).nextInt(anyInt());
    }

    @ParameterizedTest
    @MethodSource("getStartingPositionFinalExpectedPositionAndHousePosition")
    public void pawnWalksToNeighborPositionOfNearestUnBuiltStructure(
        final Position startingPosition,
        final Position expectedPosition,
        final Position housePosition) {
        // Arrange
        final Stack<Position> path = new Stack<>();
        path.add(expectedPosition);

        final IStructure mockStructure = mock(IStructure.class);
        when(mockStructure.getPosition()).thenReturn(housePosition);

        final IPathFinder mockPathFinder = mock(IPathFinder.class);
        when(mockPathFinder.path(any(), eq(expectedPosition))).thenReturn(path);

        final IStructureFinder mockFinder = mock(IStructureFinder.class);
        when(mockFinder.getNearbyIncompleteStructure(any())).thenReturn(Optional.of(mockStructure));

        final Pawn pawn = new Pawn(startingPosition,
                                   new Position(),
                                   null,
                                   mockPathFinder,
                                   mockFinder);

        // Act
        // Find and calculate neighbor position of house
        pawn.update(0.1f);

        // Assert
        assertThat(pawn.getFinalDestination()).isEqualTo(expectedPosition);
    }

    @Test
    public void pawnFindsANewStructureWhenNewStructureIsPlaced() {
        // Arrange
        final IPathFinder mockPathFinder = mock(IPathFinder.class);
        final IStructureFinder mockFinder = mock(IStructureFinder.class);

        final Position initialPosition = new Position(10f, 20f);
        final IStructure initialStructure = mock(IStructure.class);
        when(initialStructure.getPosition()).thenReturn(initialPosition);
        when(initialStructure.isCompleted()).thenReturn(true);

        final Position expectedPosition = new Position(10f, 20f);
        final IStructure expectedStructure = mock(IStructure.class);
        when(expectedStructure.getPosition()).thenReturn(expectedPosition);
        when(expectedStructure.isCompleted()).thenReturn(false);

        when(mockFinder.getNearbyIncompleteStructure(any()))
            .thenReturn(Optional.of(initialStructure))
            .thenReturn(Optional.of(expectedStructure));

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   mockPathFinder,
                                   mockFinder);

        // Act
        // Finds an incomplete structure.
        pawn.update(0.1f);
        // Does pick a new incomplete structure because current structure is completed
        pawn.update(0.1f);

        // Assert
        verify(mockFinder, times(2)).getNearbyIncompleteStructure(any());
    }

    @Test
    public void pawnDoNotTryToFindNewClosestIncompleteStructureWhenStillIncomplete() {
        // Arrange
        final IPathFinder mockPathFinder = mock(IPathFinder.class);
        final IStructureFinder mockFinder = mock(IStructureFinder.class);

        final IStructure incompleteStructure = mock(IStructure.class);

        final Position expectedPosition = new Position(10f, 20f);

        when(mockFinder.getNearbyIncompleteStructure(any())).thenReturn(Optional.of(
            incompleteStructure));

        when(incompleteStructure.getPosition()).thenReturn(expectedPosition);

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   mockPathFinder,
                                   mockFinder);

        // Act
        // Finds an incomplete structure.
        pawn.update(0.1f);
        // Doesn't pick a new incomplete structure while current structure is incomplete
        pawn.update(0.1f);

        // Assert
        verify(mockFinder, times(1)).getNearbyIncompleteStructure(any());
    }

    @Test
    public void pawnTriesToFindNewClosestStructureWhenNearestIsCompleted() {
        // Arrange
        final IPathFinder mockPathFinder = mock(IPathFinder.class);
        final IStructureFinder mockFinder = mock(IStructureFinder.class);

        final IStructure mockStructureA = mock(IStructure.class);
        final IStructure mockStructureB = mock(IStructure.class);

        final Position positionA = new Position(10f, 20f);
        final Position positionB = new Position(0f, 5f);

        when(mockFinder.getNearbyIncompleteStructure(any()))
            .thenReturn(Optional.of(mockStructureA))
            .thenReturn(Optional.of(mockStructureB));

        when(mockStructureA.getPosition()).thenReturn(positionA);
        when(mockStructureB.getPosition()).thenReturn(positionB);

        when(mockStructureA.isCompleted()).thenReturn(true);

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   mockPathFinder,
                                   mockFinder);

        // Act
        // Finds mockStructureA
        pawn.update(0.1f);
        // Discard mockStructureA, finds mockStructureB
        pawn.update(0.1f);

        // Assert
        verify(mockFinder, times(2)).getNearbyIncompleteStructure(any());
    }

    @Test
    public void pawnLooksForANewDestinationWhenItIsNotEqualToNeighborOfClosestStructuresPosition() {
        // Arrange
        final IPathFinder mockPathFinder = mock(IPathFinder.class);
        final IStructureFinder mockFinder = mock(IStructureFinder.class);

        final IStructure mockStructure = mock(IStructure.class);

        final Position structurePosition = new Position(10f, 20f);

        when(mockFinder.getNearbyIncompleteStructure(any())).thenReturn(Optional.of(mockStructure));

        when(mockPathFinder.path(any(), any())).thenReturn(List.of(new Position(0f, 0f),
                                                                   new Position(1f, 1f),
                                                                   new Position(2f, 2f),
                                                                   new Position(3f, 3f)));

        when(mockStructure.getPosition()).thenReturn(structurePosition);

        when(mockStructure.isCompleted()).thenReturn(false);

        final Pawn pawn = new Pawn(new Position(),
                                   new Position(),
                                   new Random(),
                                   mockPathFinder,
                                   mockFinder);

        // Act
        // Finds A
        pawn.update(0.1f);
        // Discard A, find B
        pawn.update(0.1f);

        // Assert
        verify(mockPathFinder, times(3)).path(any(), any());
    }

    @Test
    public void addBeingIncreasesBeingCount() {
        // Arrange
        final Iterable<Position> vacantPositions = List.of(new Position(0, 0));
        final AbstractBeingGroup colony = new Colony(vacantPositions);

        final IBeing being = createBeing();

        // Act
        final int before = colony.getBeings().size();
        colony.addBeing(being);
        final int after = colony.getBeings().size();

        // Assert
        assertThat(before).isEqualTo(1);
        assertThat(after).isEqualTo(2);
    }

    @Test
    public void sameObjectAfterDeserialization() throws ClassNotFoundException, IOException {
        // Arrange
        final IBeing being = createBeing();

        // Act
        final byte[] serializedBeing = InMemorySerialize.serialize(being);
        final IBeing deserializedBeing = (IBeing) InMemorySerialize.deserialize(serializedBeing);

        // Assert
        assertThat(being).isEqualTo(deserializedBeing);
    }

    /**
     * A test role that does nothing.
     */
    private static class NothingRole extends AbstractRole {

        @Override
        public RoleType getType() {
            return null;
        }

        @Override
        protected Collection<IActionSource> getTaskGenerators() {
            final IAction nothingTask = MockFactory.createAction(false, true);
            return List.of(performer -> nothingTask);
        }

    }

}
