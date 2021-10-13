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
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionGenerator;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleTests {

    public static Stream<Arguments> getEqualRoles() {
        final IResourceFinder resourceFinder = mock(IResourceFinder.class);
        final LumberjackRole sameLumberjack = new LumberjackRole(resourceFinder);
        return Stream.of(
            Arguments.of(sameLumberjack, sameLumberjack),
            Arguments.of(new LumberjackRole(resourceFinder), new LumberjackRole(resourceFinder)),
            Arguments.of(new FarmerRole(), new FarmerRole()),
            Arguments.of(new GuardRole(), new GuardRole()),
            Arguments.of(new FisherRole(), new FisherRole()),
            Arguments.of(new BuilderRole(), new BuilderRole()),
            Arguments.of(new IdleRole(mock(IWorld.class)), new IdleRole(mock(IWorld.class))));
    }

    public static Stream<Arguments> getUnequalRoles() {
        final IResourceFinder resourceFinder = mock(IResourceFinder.class);
        return Stream.of(
            Arguments.of(new LumberjackRole(resourceFinder), new FarmerRole()),
            Arguments.of(new FarmerRole(), new LumberjackRole(resourceFinder)),
            Arguments.of(new FisherRole(), new BuilderRole()),
            Arguments.of(new BuilderRole(), new FisherRole()),
            Arguments.of(new BuilderRole(), null));
    }

    public static Stream<Arguments> getRoleAndNames() {
        final IResourceFinder resourceFinder = mock(IResourceFinder.class);
        return Stream.of(
            Arguments.of(new LumberjackRole(resourceFinder), "Lumberjack"),
            Arguments.of(new FarmerRole(), "Farmer"),
            Arguments.of(new GuardRole(), "Guard"));
    }

    @BeforeEach
    public void setup() {
        RoleFactory.setWorld(mock(IWorld.class));
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
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
        // Behaves as if completed first time, and as if not completed second time.
        when(flipper.isCompleted(any())).thenReturn(true).thenReturn(false);
        final IAction secondCompleted = mockTask(true);
        final IAction thirdCompleted = mockTask(true);
        final AbstractRole role = mockTestRole(flipper, secondCompleted, thirdCompleted);
        final ITaskPerformer performer = mock(ITaskPerformer.class);

        // Act
        final IAction action = role.obtainNextTask(performer);

        // Assert
        assertThat(action).isSameAs(flipper);
    }

    private IAction mockTask(final boolean completed) {
        final IAction task = mock(IAction.class);
        when(task.isCompleted(any())).thenReturn(completed);
        return task;
    }

    private AbstractRole mockTestRole(final IAction... params) {
        final List<IAction> tasks = List.of(params);
        final List<IActionGenerator> taskGenerators = tasks
            .stream()
            .map(task -> (IActionGenerator) performer -> task)
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
        final ITaskPerformer performer = mock(ITaskPerformer.class);

        // Act
        final IAction actualFirst = role.obtainNextTask(performer);
        // Simulate first task completing between this call and the next.
        when(first.isCompleted(performer)).thenReturn(true);
        final IAction actualSecond = role.obtainNextTask(performer);
        // Simulate second task completing between this call and the next.
        when(actualSecond.isCompleted(performer)).thenReturn(true);
        final IAction actualThird = role.obtainNextTask(performer);

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
        final ITaskPerformer performer = mock(ITaskPerformer.class);

        // Act
        final IAction actualTask = role.obtainNextTask(performer);

        // Assert
        assertThat(actualTask).isEqualTo(uncompletedTask);
    }

    @Test
    public void obtainNextTaskReturnsSameTaskWhenItIsNotYetCompleted() {
        // Arrange
        final IAction uncompletedTask = mockTask(false);
        final IAction nextTask = mockTask(false);
        final AbstractRole role = mockTestRole(uncompletedTask, nextTask);
        final ITaskPerformer performer = mock(ITaskPerformer.class);

        // Act
        final IAction actualTaskFirstTime = role.obtainNextTask(performer);
        final IAction actualTaskSecondTime = role.obtainNextTask(performer);

        // Assert
        assertThat(actualTaskFirstTime).isEqualTo(actualTaskSecondTime).isEqualTo(uncompletedTask);
    }

    /**
     * Fake role that returns a custom set of task-generating actionables.
     */
    private static class TestRole extends AbstractRole {

        private final Collection<IActionGenerator> taskGenerators;

        TestRole(final Collection<IActionGenerator> taskGenerators) {
            this.taskGenerators = taskGenerators;
        }

        @Override
        public RoleType getType() {
            return null;
        }

        @Override
        protected Collection<IActionGenerator> getTaskGenerators() {
            return taskGenerators;
        }

    }

}
