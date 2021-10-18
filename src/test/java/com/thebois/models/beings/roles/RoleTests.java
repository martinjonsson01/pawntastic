package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionSource;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleTests {

    private IWorld world;

    public static Stream<Arguments> getEqualRoles() {
        final IResourceFinder resourceFinder = mock(IResourceFinder.class);
        final IWorld world = mock(IWorld.class);
        final LumberjackRole sameLumberjack = new LumberjackRole(resourceFinder, world);
        return Stream.of(Arguments.of(sameLumberjack, sameLumberjack),
                         Arguments.of(new LumberjackRole(resourceFinder, world),
                                      new LumberjackRole(resourceFinder, world)),
                         Arguments.of(new FarmerRole(), new FarmerRole()),
                         Arguments.of(new GuardRole(), new GuardRole()),
                         Arguments.of(new FisherRole(mock(IResourceFinder.class),
                                                     mock(IWorld.class)),
                                      new FisherRole(mock(IResourceFinder.class),
                                                     mock(IWorld.class))),
                         Arguments.of(new BuilderRole(), new BuilderRole()),
                         Arguments.of(new IdleRole(mock(IWorld.class)),
                                      new IdleRole(mock(IWorld.class))));
    }

    public static Stream<Arguments> getUnequalRoles() {
        final IResourceFinder resourceFinder = mock(IResourceFinder.class);
        final IWorld world = mock(IWorld.class);
        return Stream.of(Arguments.of(new LumberjackRole(resourceFinder, world), new FarmerRole()),
                         Arguments.of(new FarmerRole(), new LumberjackRole(resourceFinder, world)),
                         Arguments.of(new FisherRole(mock(IResourceFinder.class),
                                                     mock(IWorld.class)), new BuilderRole()),
                         Arguments.of(new BuilderRole(),
                                      new FisherRole(mock(IResourceFinder.class),
                                                     mock(IWorld.class))),
                         Arguments.of(new BuilderRole(), null));
    }

    public static Stream<Arguments> getRoleAndNames() {
        final IResourceFinder resourceFinder = mock(IResourceFinder.class);
        final IWorld world = mock(IWorld.class);
        return Stream.of(Arguments.of(new LumberjackRole(resourceFinder, world), "Lumberjack"),
                         Arguments.of(new FarmerRole(), "Farmer"),
                         Arguments.of(new GuardRole(), "Guard"));
    }

    @BeforeEach
    public void setup() {
        world = mock(IWorld.class);
        RoleFactory.setWorld(world);
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        ActionFactory.setPathFinder(mock(IPathFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        ActionFactory.setPathFinder(null);
    }

    @ParameterizedTest
    @MethodSource("getRoleAndNames")
    public void getNameIsExpectedName(final AbstractRole role, final String expectedName) {
        // Act
        final String actualName = role.getName();

        // Assert
        assertThat(actualName).isEqualTo(expectedName);
    }

    @ParameterizedTest
    @MethodSource("getEqualRoles")
    public void equalsReturnsTrueWhenEqual(final AbstractRole first, final AbstractRole second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getUnequalRoles")
    public void equalsReturnsFalseWhenUnequal(final AbstractRole first, final AbstractRole second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void deepCloneCreatesCloneThatIsEqual() {
        // Arrange
        final AbstractRole original = RoleFactory.lumberjack();

        // Act
        final AbstractRole deepClone = original.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(original);
    }

    @Test
    public void obtainNextTaskReturnsFirstGeneratedTaskWhenAllInFirstBatchAreCompleted() {
        // Arrange
        final IAction flipper = mock(IAction.class);
        when(flipper.canPerform(any())).thenReturn(true);
        // Behaves as if completed first time, and as if not completed second time.
        when(flipper.isCompleted(any())).thenReturn(true).thenReturn(false);
        final IAction secondCompleted = mockTask(true);
        final IAction thirdCompleted = mockTask(true);
        final AbstractRole role = mockTestRole(flipper, secondCompleted, thirdCompleted);
        final IActionPerformer performer = mock(IActionPerformer.class);

        // Act
        final IAction action = role.obtainNextAction(performer);

        // Assert
        assertThat(action).isSameAs(flipper);
    }

    private IAction mockTask(final boolean completed) {
        return MockFactory.createAction(completed, true);
    }

    private AbstractRole mockTestRole(final IAction... params) {
        final List<IAction> tasks = List.of(params);
        final List<IActionSource> taskGenerators = tasks
            .stream()
            .map(task -> (IActionSource) performer -> task)
            .collect(Collectors.toList());
        return new TestRole(taskGenerators);
    }

    @Test
    public void obtainNextTaskReturnsTasksInOrderWhenTasksAreCompletedInSequence() {
        // Arrange
        final IAction first = mockTask(false);
        final IAction second = mockTask(false);
        final IAction third = mockTask(false);
        final AbstractRole role = mockTestRole(first, second, third);
        final IActionPerformer performer = mock(IActionPerformer.class);

        // Act
        final IAction actualFirst = role.obtainNextAction(performer);
        // Simulate first task completing between this call and the next.
        when(first.isCompleted(performer)).thenReturn(true);
        final IAction actualSecond = role.obtainNextAction(performer);
        // Simulate second task completing between this call and the next.
        when(actualSecond.isCompleted(performer)).thenReturn(true);
        final IAction actualThird = role.obtainNextAction(performer);

        // Assert
        final List<IAction> actualTasks = List.of(actualFirst, actualSecond, actualThird);
        assertThat(actualTasks).containsExactly(first, second, third);
    }

    @Test
    public void obtainNextTaskSkipsOverAlreadyCompletedTasks() {
        // Arrange
        final IAction completedTask1 = mockTask(true);
        final IAction completedTask2 = mockTask(true);
        final IAction uncompletedTask = mockTask(false);
        final AbstractRole role = mockTestRole(completedTask1, completedTask2, uncompletedTask);
        final IActionPerformer performer = mock(IActionPerformer.class);

        // Act
        final IAction actualTask = role.obtainNextAction(performer);

        // Assert
        assertThat(actualTask).isEqualTo(uncompletedTask);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenPreviouslyPerformableBecomesUnperformable() {
        // Arrange
        final IAction switchPerformableAction = MockFactory.createAction(false, true);
        when(switchPerformableAction.canPerform(any())).thenReturn(true).thenReturn(false);
        final AbstractRole role = mockTestRole(switchPerformableAction);
        final IActionPerformer performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));

        final ITile randomTile = mock(ITile.class);
        when(randomTile.getPosition()).thenReturn(new Position(10, 10));
        when(world.getRandomVacantSpot()).thenReturn(randomTile);
        final AbstractRole idleRole = RoleFactory.idle();
        final IAction idleAction = idleRole.obtainNextAction(performer);

        // Act
        role.obtainNextAction(performer);
        final IAction actualAction = role.obtainNextAction(performer);

        // Assert
        assertThat(actualAction).isEqualTo(idleAction);
    }

    @Test
    public void obtainNextActionReturnsIdleRoleActionWhenNotAbleToPerform() {
        // Arrange
        final IAction unperformableAction = MockFactory.createAction(false, false);
        final AbstractRole role = mockTestRole(unperformableAction);
        final IActionPerformer performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));

        final ITile randomTile = mock(ITile.class);
        when(randomTile.getPosition()).thenReturn(new Position(10, 10));
        when(world.getRandomVacantSpot()).thenReturn(randomTile);
        final AbstractRole idleRole = RoleFactory.idle();
        final IAction idleAction = idleRole.obtainNextAction(performer);

        // Act
        final IAction actualAction = role.obtainNextAction(performer);

        // Assert
        assertThat(actualAction).isEqualTo(idleAction);
    }

    @Test
    public void obtainNextTaskReturnsSameTaskWhenItIsNotYetCompleted() {
        // Arrange
        final IAction uncompletedTask = mockTask(false);
        final IAction nextTask = mockTask(false);
        final AbstractRole role = mockTestRole(uncompletedTask, nextTask);
        final IActionPerformer performer = mock(IActionPerformer.class);

        // Act
        final IAction actualTaskFirstTime = role.obtainNextAction(performer);
        final IAction actualTaskSecondTime = role.obtainNextAction(performer);

        // Assert
        assertThat(actualTaskFirstTime).isEqualTo(actualTaskSecondTime).isEqualTo(uncompletedTask);
    }

    /**
     * Fake role that returns a custom set of task-generating actionables.
     */
    private static class TestRole extends AbstractRole {

        private final Collection<IActionSource> taskGenerators;

        TestRole(final Collection<IActionSource> taskGenerators) {
            this.taskGenerators = taskGenerators;
        }

        @Override
        public RoleType getType() {
            return null;
        }

        @Override
        protected Collection<IActionSource> getTaskGenerators() {
            return taskGenerators;
        }

    }

}
