package com.thebois.models.beings;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
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
import com.thebois.models.inventory.Inventory;
import com.thebois.models.world.IWorld;
import com.thebois.testutils.InMemorySerialize;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

    private static final IInventory inventory = mock(IInventory.class);

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
        final AbstractRole role = new NothingRole();
        return createBeing(new Position(), role);
    }

    private static AbstractBeing createBeing(
        final Position currentPosition, final AbstractRole role) {
        return new Pawn(currentPosition.deepClone(), role, inventory);
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

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void updateDoesNotOvershootDestinationWhenDeltaTimeIsLarge(
        final Position startPosition, final Position destination) {
        // Arrange
        final AbstractBeing being = createBeing();
        being.setDestination(destination);

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
        final AbstractBeing being = createBeing();
        being.setDestination(destination);

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
        final AbstractBeing slowerBeing = createBeing();
        final AbstractBeing fasterBeing = createBeing();
        slowerBeing.setDestination(destination);
        fasterBeing.setDestination(destination);

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
        final IBeing being = new Pawn(new Position(0, 0), RoleFactory.idle(), new Inventory());

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
        public boolean equals(final Object obj) {
            return obj instanceof NothingRole;
        }

        @Override
        public RoleType getType() {
            return null;
        }

        @Override
        public AbstractRole deepClone() {
            return new NothingRole();
        }

        @Override
        protected Collection<IActionSource> getTaskGenerators() {
            final IAction nothingTask = MockFactory.createAction(false, true);
            return List.of(performer -> nothingTask);
        }

    }

    @Test
    public void beingDelegatesTryAddToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.tryAdd(any());

        // Assert
        verify(inventory, times(1)).tryAdd(any());
    }

    @Test
    public void beingDelegatesAddMultipleToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.addMultiple(any());

        // Assert
        verify(inventory, times(1)).addMultiple(any());
    }

    @Test
    public void beingDelegatesCanFitItemToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.canFitItem(any());

        // Assert
        verify(inventory, times(1)).canFitItem(any());
    }

    @Test
    public void beingDelegatesIsFullToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.isFull();

        // Assert
        verify(inventory, times(1)).isFull();
    }

    @Test
    public void beingDelegatesHasItemToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.hasItem(any());

        // Assert
        verify(inventory, times(1)).hasItem(any());
    }

    @Test
    public void beingDelegatesHasItemAmountToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.hasItem(any(), anyInt());

        // Assert
        verify(inventory, times(1)).hasItem(any(), anyInt());
    }

    @Test
    public void beingDelegatesNumberOfToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.numberOf(any());

        // Assert
        verify(inventory, times(1)).numberOf(any());
    }

    @Test
    public void beingDelegatesTakeToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.take(any());

        // Assert
        verify(inventory, times(1)).take(any());
    }

    @Test
    public void beingDelegatesTakeAmountToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.takeAmount(any(), anyInt());

        // Assert
        verify(inventory, times(1)).takeAmount(any(), anyInt());
    }

    @Test
    public void beingDelegatesTakeNextItemToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.takeNextItem();

        // Assert
        verify(inventory, times(1)).takeNextItem();
    }

    @Test
    public void beingDelegatesIsEmptyToInventory() {
        // Arrange
        final AbstractBeing being = createBeing();

        // Act
        being.isEmpty();

        // Assert
        verify(inventory, times(1)).isEmpty();
    }

}
