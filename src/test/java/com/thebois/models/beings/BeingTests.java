package com.thebois.models.beings;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionGenerator;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

    public static Stream<Arguments> getEqualBeings() {
        RoleFactory.setWorld(mock(IWorld.class));
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

    private static AbstractBeing createBeing(
        final float startX, final float startY, final RoleType roleType) {
        final AbstractRole role = RoleFactory.fromType(roleType);
        return createBeing(new Position(startX, startY), role);
    }

    private static AbstractBeing createBeing() {
        final AbstractRole role = RoleFactory.idle();
        return createBeing(new Position(), role);
    }

    private static AbstractBeing createBeing(
        final Position currentPosition, final AbstractRole role) {
        return new Pawn(currentPosition.deepClone(), role);
    }

    public static Stream<Arguments> getNotEqualBeings() {
        RoleFactory.setWorld(mock(IWorld.class));
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
        RoleFactory.setWorld(mock(IWorld.class));
        ActionFactory.setPathFinder(mock(IPathFinder.class));
    }

    @AfterEach
    public void teardown() {
        ActionFactory.setPathFinder(null);
        RoleFactory.setWorld(null);
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
        being.update();

        // Assert
        final Position actualPosition = being.getPosition();
        final float distanceToDestinationAfter = actualPosition.distanceTo(being.getDestination());
        assertThat(distanceToDestinationAfter).isLessThan(distanceToDestinationBefore);
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
        when(role.obtainNextTask()).thenReturn(task);

        // Act
        being.update();

        // Assert
        verify(role, times(1)).obtainNextTask();
    }

    @Test
    public void updatePerformsTask() {
        // Arrange
        final AbstractRole role = mock(AbstractRole.class);
        final AbstractBeing being = createBeing();
        final IAction task = mock(IAction.class);
        being.setRole(role);
        when(role.obtainNextTask()).thenReturn(task);

        // Act
        being.update();

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

    /**
     * A test role that does nothing.
     */
    private static class NothingRole extends AbstractRole {

        @Override
        public RoleType getType() {
            return null;
        }

        @Override
        protected Collection<IActionGenerator> getTaskGenerators() {
            final IAction nothingTask = mock(IAction.class);
            when(nothingTask.isCompleted()).thenReturn(false);
            return List.of(() -> nothingTask);
        }

    }

}
