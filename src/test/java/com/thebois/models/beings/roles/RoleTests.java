package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.beings.tasks.ITask;
import com.thebois.models.beings.tasks.ITaskGenerator;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleTests {

    public static Stream<Arguments> getEqualRoles() {
        final LumberjackRole sameLumberjack = new LumberjackRole();
        return Stream.of(
            Arguments.of(sameLumberjack, sameLumberjack),
            Arguments.of(new LumberjackRole(), new LumberjackRole()),
            Arguments.of(new FarmerRole(), new FarmerRole()),
            Arguments.of(new GuardRole(), new GuardRole()),
            Arguments.of(new MinerRole(), new MinerRole()),
            Arguments.of(new FisherRole(), new FisherRole()),
            Arguments.of(new BuilderRole(), new BuilderRole()),
            Arguments.of(new IdleRole(), new IdleRole()));
    }

    public static Stream<Arguments> getUnequalRoles() {
        return Stream.of(
            Arguments.of(new LumberjackRole(), new FarmerRole()),
            Arguments.of(new FarmerRole(), new LumberjackRole()),
            Arguments.of(new GuardRole(), new MinerRole()),
            Arguments.of(new MinerRole(), new GuardRole()),
            Arguments.of(new FisherRole(), new BuilderRole()),
            Arguments.of(new BuilderRole(), new FisherRole()),
            Arguments.of(new BuilderRole(), null));
    }

    public static Stream<Arguments> getRoleAndNames() {
        return Stream.of(
            Arguments.of(new LumberjackRole(), "Lumberjack"),
            Arguments.of(new FarmerRole(), "Farmer"),
            Arguments.of(new GuardRole(), "Guard"),
            Arguments.of(new MinerRole(), "Miner"));
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
    public void obtainNextTaskReturnsTasksInOrderWhenTasksAreCompletedInSequence() {
        // Arrange
        final ITask first = mockTask(false);
        final ITask second = mockTask(false);
        final ITask third = mockTask(false);
        final AbstractRole role = mockTestRole(first, second, third);

        // Act
        final ITask actualFirst = role.obtainNextTask();
        // Simulate first task completing between this call and the next.
        when(first.isCompleted()).thenReturn(true);
        final ITask actualSecond = role.obtainNextTask();
        // Simulate second task completing between this call and the next.
        when(actualSecond.isCompleted()).thenReturn(true);
        final ITask actualThird = role.obtainNextTask();

        // Assert
        final List<ITask> actualTasks = List.of(actualFirst, actualSecond, actualThird);
        assertThat(actualTasks).containsExactly(first, second, third);
    }

    private ITask mockTask(final boolean completed) {
        final ITask task = mock(ITask.class);
        when(task.isCompleted()).thenReturn(completed);
        return task;
    }

    private AbstractRole mockTestRole(final ITask... params) {
        final List<ITask> tasks = List.of(params);
        final List<ITaskGenerator> taskGenerators =
            tasks.stream().map(task -> (ITaskGenerator) () -> task).collect(Collectors.toList());
        return new TestRole(taskGenerators);
    }

    @Test
    public void obtainNextTaskSkipsOverAlreadyCompletedTasks() {
        // Arrange
        final ITask completedTask1 = mockTask(true);
        final ITask completedTask2 = mockTask(true);
        final ITask uncompletedTask = mockTask(false);
        final AbstractRole role = mockTestRole(completedTask1, completedTask2, uncompletedTask);

        // Act
        final ITask actualTask = role.obtainNextTask();

        // Assert
        assertThat(actualTask).isEqualTo(uncompletedTask);
    }

    @Test
    public void obtainNextTaskReturnsSameTaskWhenItIsNotYetCompleted() {
        // Arrange
        final ITask uncompletedTask = mockTask(false);
        final ITask nextTask = mockTask(false);
        final AbstractRole role = mockTestRole(uncompletedTask, nextTask);

        // Act
        final ITask actualTaskFirstTime = role.obtainNextTask();
        final ITask actualTaskSecondTime = role.obtainNextTask();

        // Assert
        assertThat(actualTaskFirstTime).isEqualTo(actualTaskSecondTime).isEqualTo(uncompletedTask);
    }

    /**
     * Fake role that returns a custom set of task-generating actionables.
     */
    private static class TestRole extends AbstractRole {

        private final Collection<ITaskGenerator> taskGenerators;

        TestRole(final Collection<ITaskGenerator> taskGenerators) {
            this.taskGenerators = taskGenerators;
        }

        @Override
        public RoleType getType() {
            return null;
        }

        @Override
        protected Collection<ITaskGenerator> getTaskGenerators() {
            return taskGenerators;
        }

    }

}
