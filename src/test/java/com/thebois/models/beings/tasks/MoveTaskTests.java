package com.thebois.models.beings.tasks;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import com.thebois.ColonyManagement;
import com.thebois.listeners.events.ObstaclePlacedEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.pathfinding.IPathFinder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoveTaskTests {

    private IPathFinder pathFinder;

    public static Stream<Arguments> getPositionsAndDestinations() {
        return Stream.of(Arguments.of(new Position(0, 0), new Position(0, 1)),
                         Arguments.of(new Position(0, 0), new Position(1, 0)),
                         Arguments.of(new Position(0, 0), new Position(99, 99)),
                         Arguments.of(new Position(0, 0), new Position(-1, -1)),
                         Arguments.of(new Position(0, 0), new Position(-99, -99)),
                         Arguments.of(new Position(123, 456), new Position(0, 0)),
                         Arguments.of(new Position(456, 789), new Position(0, 0)));
    }

    public static Stream<Arguments> getEqualMoveTasks() {
        TaskFactory.setPathFinder(mock(IPathFinder.class));
        final ITask sameInstance = TaskFactory.createMoveTo(new Position(0, 1));
        return Stream.of(Arguments.of(sameInstance, sameInstance),
                         Arguments.of(TaskFactory.createMoveTo(new Position(0, 1)),
                                      TaskFactory.createMoveTo(new Position(0, 1))));
    }

    public static Stream<Arguments> getNotEqualMoveTasks() {
        TaskFactory.setPathFinder(mock(IPathFinder.class));
        return Stream.of(Arguments.of(TaskFactory.createMoveTo(new Position(0, 0)),
                                      TaskFactory.createMoveTo(new Position(0, 1))),
                         Arguments.of(TaskFactory.createMoveTo(new Position(0, 1)), null),
                         Arguments.of(TaskFactory.createMoveTo(new Position(0, 1)),
                                      mock(ITask.class)));
    }

    @BeforeEach
    public void setup() {
        pathFinder = mock(IPathFinder.class);
        TaskFactory.setPathFinder(pathFinder);
    }

    @AfterEach
    public void restore() {
        TaskFactory.setPathFinder(null);
    }

    @ParameterizedTest
    @MethodSource("getEqualMoveTasks")
    public void equalsIsTrueForEqualTasks(final ITask first, final ITask second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualMoveTasks")
    public void equalsIsFalseForNotEqualTasks(final ITask first, final ITask second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void hashCodeIsEqualWhenPositionIsEqual() {
        // Arrange
        final Position destination = new Position(10, 10);
        final ITask first = TaskFactory.createMoveTo(destination);
        final ITask second = TaskFactory.createMoveTo(destination);

        // Act
        final int firstHash = first.hashCode();
        final int secondHash = second.hashCode();

        // Assert
        assertThat(firstHash).isEqualTo(secondHash);
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndDestinations")
    public void performSetsDestinationToEveryStepInPathAfterMovingToPreviousStep(
        final Position start, final Position end) {
        // Arrange
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> path = List.of(new Position(1, 1), new Position(2, 2), end);
        when(pathFinder.path(start, end)).thenReturn(path);

        final ITask task = TaskFactory.createMoveTo(end);

        final ArgumentCaptor<Position> destinationCaptor = ArgumentCaptor.forClass(Position.class);

        // Act
        for (final Position pathPosition : path) {
            task.perform(performer);
            // Simulate moving to path position.
            when(performer.getPosition()).thenReturn(pathPosition);
        }

        // Assert
        verify(performer, times(path.size())).setDestination(destinationCaptor.capture());
        final List<Position> destinations = destinationCaptor.getAllValues();
        assertThat(destinations).containsExactlyElementsOf(path);
    }

    @Test
    public void pathIsRecalculatedWhenObstacleIsInWay() {
        // Arrange
        final Position start = new Position(0, 0);
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> path = List.of(new Position(1, 1), new Position(2, 2), end);
        when(pathFinder.path(start, end)).thenReturn(path);

        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(1, 1);

        final ITask task = TaskFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        ColonyManagement.BUS.post(obstacleEvent);

        // Assert
        verify(pathFinder, times(2)).path(start, end);
    }

    @Test
    public void pathIsNotRecalculatedWhenObstacleIsOutOfWay() {
        // Arrange
        final Position start = new Position(0, 0);
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> path = List.of(new Position(1, 1), new Position(2, 2), end);
        when(pathFinder.path(start, end)).thenReturn(path);

        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(0, 1);

        final ITask task = TaskFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        ColonyManagement.BUS.post(obstacleEvent);

        // Assert
        verify(pathFinder, times(1)).path(start, end);
    }

    @Test
    public void performDoesNothingWhenAtEnd() {
        // Arrange
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(end);

        final ITask task = TaskFactory.createMoveTo(end);

        // Act
        task.perform(performer);

        // Assert
        verify(performer, times(0)).setDestination(any());
    }

    @Test
    public void isCompletedIsTrueWhenAtEnd() {
        // Arrange
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(end);

        final ITask task = TaskFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        final boolean completed = task.isCompleted();

        // Assert
        assertThat(completed).isTrue();
    }

    @Test
    public void isCompletedIsFalseWhenAtStart() {
        // Arrange
        final Position start = new Position(0, 0);
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> path = List.of(new Position(1, 1), new Position(2, 2), end);
        when(pathFinder.path(start, end)).thenReturn(path);

        final ITask task = TaskFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        final boolean completed = task.isCompleted();

        // Assert
        assertThat(completed).isFalse();
    }

}
