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

import com.thebois.Pawntastic;
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
import com.thebois.models.inventory.items.IConsumableItem;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.IWorld;
import com.thebois.testutils.InMemorySerialize;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

    private static final float HUNGER_RATE = 1f;
    private AbstractRole role;
    private AbstractBeing being;

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
                         Arguments.of(createBeing(0, 0, RoleType.MINER),
                                      createBeing(0, 0, RoleType.MINER)),
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
        final AbstractRole hungerRole = new NothingRole();
        return createBeing(new Position(startX, startY), role, hungerRole);
    }

    private static AbstractBeing createBeing() {
        final AbstractRole role = new NothingRole();
        return createBeing(new Position(), role);
    }

    private static AbstractBeing createBeing(
        final Position currentPosition, final AbstractRole role, final AbstractRole hungerRole) {
        return new Pawn(currentPosition, role, hungerRole);
    }

    private static AbstractBeing createBeing(
        final Position currentPosition, final AbstractRole role) {
        final AbstractRole hungerRole = new NothingRole();
        return new Pawn(currentPosition, role, hungerRole);
    }

    public static Stream<Arguments> getNotEqualBeings() {
        mockFactoryDependencies();
        return Stream.of(Arguments.of(createBeing(0, 0, RoleType.MINER),
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

        role = new NothingRole();
        being = createBeing(new Position(), role);
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
        being.setDestination(destination);
        final Position start = being.getPosition();
        final float distanceToDestinationBefore = start.distanceTo(being.getDestination());

        // Act
        being.update(0.1f);

        // Assert
        final Position actualPosition = being.getPosition();
        final float distanceToDestinationAfter = actualPosition.distanceTo(being.getDestination());
        assertThat(distanceToDestinationAfter).isLessThan(distanceToDestinationBefore);
    }

    @Test
    public void addItemDoesNotThrow() {
        // Arrange
        final IActionPerformer being = createBeing();
        final IItem item = mock(IItem.class);

        // Act
        being.addItem(item);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void updateDoesNotOvershootDestinationWhenDeltaTimeIsLarge(
        final Position startPosition, final Position destination) {
        // Arrange
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
        // Assert
        assertThatThrownBy(() -> being.setRole(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateGetsActionFromRole() {
        // Arrange
        final AbstractRole mockRole = mock(AbstractRole.class);
        final IAction action = mock(IAction.class);
        being.setRole(mockRole);
        when(mockRole.obtainNextAction(being)).thenReturn(action);

        // Act
        being.update(0.1f);

        // Assert
        verify(mockRole, times(1)).obtainNextAction(being);
    }

    @Test
    public void updatePerformsAction() {
        // Arrange
        final AbstractRole mockRole = mock(AbstractRole.class);
        final IAction action = mock(IAction.class);
        being.setRole(mockRole);
        when(mockRole.obtainNextAction(being)).thenReturn(action);

        // Act
        being.update(0.1f);

        // Assert
        verify(action, times(1)).perform(being, 0.1f);
    }

    @Test
    public void hashCodeReturnsSameIfEqual() {
        // Arrange
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
        final IBeing first = createBeing(new Position(0, 0), RoleFactory.miner());
        final IBeing second = createBeing(new Position(123, 123), RoleFactory.idle());

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isNotEqualTo(secondHashCode);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
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
        final AbstractBeingGroup colony = new Colony(vacantPositions, Pawntastic::getEventBus);

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
        // Act
        final byte[] serializedBeing = InMemorySerialize.serialize(being);
        final IBeing deserializedBeing = (IBeing) InMemorySerialize.deserialize(serializedBeing);

        // Assert
        assertThat(being).isEqualTo(deserializedBeing);
    }

    @Test
    public void doesNotLoseHealthWhenHasFoodInInventoryAndTimePasses() {
        // Arrange
        final float timeToPass = 50f;
        final float startHealthRatio = being.getHealthRatio();

        final IConsumableItem food = MockFactory.createEdibleItem(0.1f, 100f, ItemType.FISH);
        being.addItem(food);

        // Act
        being.update(timeToPass);
        // Calling method in order for pawn to not die instantly.
        being.update(timeToPass);
        final float endHealthRatio = being.getHealthRatio();

        //Assert
        assertThat(endHealthRatio).isEqualTo(startHealthRatio);
    }

    @Test
    public void doesNotPerformRoleActionWhenNoFoodInInventoryAndHungry() {
        // Arrange
        final float timeUntilHungry = 50 * HUNGER_RATE;

        final AbstractRole mockRole = mock(AbstractRole.class);
        final IAction action = mock(IAction.class);
        being.setRole(mockRole);
        when(mockRole.obtainNextAction(being)).thenReturn(action);

        // Act
        being.update(timeUntilHungry);

        // Assert
        verify(action, times(0)).perform(being, 0.1f);
    }

    @Test
    public void performsHungerRoleActionWhenNoFoodInInventoryAndHungry() {
        // Arrange
        final float timeUntilHungry = 50 * HUNGER_RATE;

        final AbstractRole role = mock(AbstractRole.class);
        final AbstractRole hungerRole = mock(AbstractRole.class);
        final IAction action = mock(IAction.class);
        final IAction hungerAction = mock(IAction.class);
        being = createBeing(new Position(), role, hungerRole);
        when(role.obtainNextAction(being)).thenReturn(action);
        when(hungerRole.obtainNextAction(being)).thenReturn(hungerAction);

        // Act
        being.update(timeUntilHungry);

        // Assert
        verify(action, times(0)).perform(eq(being), anyFloat());
        verify(hungerAction, times(1)).perform(being, timeUntilHungry);
    }

    @Test
    public void performsAssignedRoleActionAgainAfterHavingFoundFood() {
        // Arrange
        final float timeUntilHungry = 50 * HUNGER_RATE;

        final AbstractRole role = mock(AbstractRole.class);
        final AbstractRole hungerRole = mock(AbstractRole.class);
        final IAction action = mock(IAction.class);
        final IAction hungerAction = mock(IAction.class);
        being = createBeing(new Position(), role, hungerRole);
        when(role.obtainNextAction(being)).thenReturn(action);
        when(hungerRole.obtainNextAction(being)).thenReturn(hungerAction);

        being.update(timeUntilHungry);
        // Simulate finding food.
        being.addItem(MockFactory.createEdibleItem(1f, 10f, ItemType.FISH));

        final float deltaTime = 0.1f;

        // Act
        being.update(deltaTime);

        // Assert
        verify(action, times(1)).perform(being, deltaTime);
        verify(hungerAction, times(1)).perform(being, timeUntilHungry);
    }

    @Test
    public void doesNotEatFoodWhenNotHungry() {
        // Arrange
        final float timeToPass = 0.1f;

        final IConsumableItem food = MockFactory.createEdibleItem(0.1f, 100f, ItemType.FISH);
        being.addItem(food);

        // Act
        being.update(timeToPass);

        //Assert
        verify(food, times(0)).getNutrientValue();
    }

    @Test
    public void beingsLosesHealthWhenHasNoFoodInInventoryAndTimePasses() {
        // Arrange
        final float timeToPass = 50f;
        final float startHealthRatio = being.getHealthRatio();

        // Act
        being.update(timeToPass);
        // Calling method in order for pawn to not die instantly.
        being.update(timeToPass);
        final float endHealthRatio = being.getHealthRatio();

        //Assert
        assertThat(endHealthRatio).isLessThan(startHealthRatio).isGreaterThan(0);
    }

    @Test
    public void beingHealthRatioIsZeroAfterLongTimePasses() {
        // Arrange
        final float timeToPass = 1000f;
        final float expectedHealthRatio = 0;

        // Act
        being.update(timeToPass);
        final float endHealthRatio = being.getHealthRatio();

        //Assert
        assertThat(endHealthRatio).isEqualTo(expectedHealthRatio);
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

}
