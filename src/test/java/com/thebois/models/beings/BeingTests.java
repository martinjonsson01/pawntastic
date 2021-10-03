package com.thebois.models.beings;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.models.beings.tasks.ITask;
import com.thebois.models.beings.tasks.TaskFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeingTests {

    public static Stream<Arguments> getEqualBeings() {
        final AbstractBeing beingA = createBeing(0, 0, RoleType.BUILDER);
        final AbstractBeing beingB = createBeing(0, 0, RoleType.BUILDER);
        final AbstractBeing beingC = createBeing(0, 0, RoleType.BUILDER);
        return Stream.of(Arguments.of(createBeing(0, 0, RoleType.FARMER),
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

    private static AbstractBeing createBeing(
        final Position currentPosition, final AbstractRole role) {
        return new Pawn(currentPosition.deepClone(), role);
    }

    public static Stream<Arguments> getNotEqualBeings() {
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
        TaskFactory.setPathFinder(mock(IPathFinder.class));
    }

    @AfterEach
    public void teardown() {
        TaskFactory.setPathFinder(null);
    }

    @ParameterizedTest
    @MethodSource("getDestinations")
    public void updateMovesTowardsDestinationWhenNotAtDestination(final Position destination) {
        // Arrange
        final Position start = new Position();
        final AbstractBeing being = createBeing(start);
        being.setDestination(destination);
        final float distanceToDestinationBefore = start.distanceTo(destination);

        // Act
        being.update();

        // Assert
        final Position actualPosition = being.getPosition();
        final float distanceToDestinationAfter = actualPosition.distanceTo(destination);
        assertThat(distanceToDestinationAfter).isLessThan(distanceToDestinationBefore);
    }

    private static AbstractBeing createBeing(final Position position) {
        final AbstractRole role = RoleFactory.idle();
        return createBeing(position, role);
    }

    @Test
    public void setRoleWithNullThrowsException() {
        // Arrange
        final IBeing being = createBeing(new Position());

        // Assert
        assertThatThrownBy(() -> being.setRole(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateGetsTaskFromRole() {
        // Arrange
        final AbstractRole role = mock(AbstractRole.class);
        final AbstractBeing being = createBeing();
        final ITask task = mock(ITask.class);
        being.setRole(role);
        when(role.obtainNextTask()).thenReturn(task);

        // Act
        being.update();

        // Assert
        verify(role, times(1)).obtainNextTask();
    }

    private static AbstractBeing createBeing() {
        return createBeing(new Position());
    }

    @Test
    public void updatePerformsTask() {
        // Arrange
        final AbstractRole role = mock(AbstractRole.class);
        final AbstractBeing being = createBeing();
        final ITask task = mock(ITask.class);
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
        final Position currentPosition = new Position(0, 0);
        final AbstractRole role = RoleFactory.farmer();
        final IBeing first = createBeing(currentPosition.deepClone());
        first.setRole(role);
        final IBeing second = createBeing(currentPosition.deepClone());
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
        final IBeing first = createBeing(new Position(0, 0));
        first.setRole(role);
        final IBeing second = createBeing(new Position(123, 123));

        // Act
        final int firstHashCode = first.hashCode();
        final int secondHashCode = second.hashCode();

        // Assert
        assertThat(firstHashCode).isNotEqualTo(secondHashCode);
    }

    @Test
    public void equalsReturnsFalseForOtherType() {
        // Arrange
        final IBeing being = createBeing(new Position(0, 0));
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

}
