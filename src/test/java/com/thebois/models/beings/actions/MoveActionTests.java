package com.thebois.models.beings.actions;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.ObstaclePlacedEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.pathfinding.IPathFinder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoveActionTests {

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
        ActionFactory.setPathFinder(mock(IPathFinder.class));
        final IAction sameInstance = ActionFactory.createMoveTo(new Position(0, 1));
        return Stream.of(Arguments.of(sameInstance, sameInstance),
                         Arguments.of(ActionFactory.createMoveTo(new Position(0, 1)),
                                      ActionFactory.createMoveTo(new Position(0, 1))));
    }

    public static Stream<Arguments> getNotEqualMoveTasks() {
        ActionFactory.setPathFinder(mock(IPathFinder.class));
        return Stream.of(Arguments.of(ActionFactory.createMoveTo(new Position(0, 0)),
                                      ActionFactory.createMoveTo(new Position(0, 1))),
                         Arguments.of(ActionFactory.createMoveTo(new Position(0, 1)), null),
                         Arguments.of(ActionFactory.createMoveTo(new Position(0, 1)),
                                      mock(IAction.class)));
    }

    @BeforeEach
    public void setup() {
        pathFinder = mock(IPathFinder.class);
        ActionFactory.setPathFinder(pathFinder);
    }

    @AfterEach
    public void restore() {
        ActionFactory.setPathFinder(null);
    }

    @ParameterizedTest
    @MethodSource("getEqualMoveTasks")
    public void equalsIsTrueForEqualTasks(final IAction first, final IAction second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualMoveTasks")
    public void equalsIsFalseForNotEqualTasks(final IAction first, final IAction second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void canPerformReturnsTrueWhenDestinationIsReachable() {
        // Arrange
        final Position start = new Position(0, 0);
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> path = List.of();
        when(pathFinder.path(start, end)).thenReturn(path);

        final IAction task = ActionFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        final boolean isCompleted = task.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isTrue();
    }

    @Test
    public void performCompletesTaskWhenDestinationIsUnreachable() {
        // Arrange
        final Position start = new Position(0, 0);
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> path = List.of(new Position(1, 1), new Position(2, 2), end);
        when(pathFinder.path(start, end)).thenReturn(path);

        final IAction task = ActionFactory.createMoveTo(end);

        // Act
        final boolean canPerform = task.canPerform(performer);

        // Assert
        assertThat(canPerform).isTrue();
    }

    @Test
    public void hashCodeIsEqualWhenPositionIsEqual() {
        // Arrange
        final Position destination = new Position(10, 10);
        final IAction first = ActionFactory.createMoveTo(destination);
        final IAction second = ActionFactory.createMoveTo(destination);

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

        final IAction task = ActionFactory.createMoveTo(end);

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
    public void actionIsCompletedWhenNewObstacleBlocksAllPossiblePathsToDestination() {
        // Arrange
        final Position start = new Position(0, 0);
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> workingPath = List.of(new Position(1, 1), new Position(2, 2), end);
        final List<Position> noPath = List.of();
        when(pathFinder.path(any(), any())).thenReturn(workingPath).thenReturn(noPath);

        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(1, 1);

        final IAction task = ActionFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        Pawntastic.BUS.post(obstacleEvent);
        final boolean completed = task.isCompleted(performer);

        // Assert
        assertThat(completed).isTrue();
    }

    @Test
    public void entirePathIsRecalculatedWhenObstacleIsInWayAtStartOfPath() {
        // Arrange
        final Position start = new Position(0, 0);
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(start);

        final List<Position> path = List.of(new Position(1, 1), new Position(2, 2), end);
        when(pathFinder.path(any(), any())).thenReturn(path);

        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(1, 1);

        final IAction task = ActionFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        Pawntastic.BUS.post(obstacleEvent);

        // Assert
        verify(pathFinder, times(2)).path(any(), any());
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

        final IAction task = ActionFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        Pawntastic.BUS.post(obstacleEvent);

        // Assert
        verify(pathFinder, times(1)).path(start, end);
    }

    @Test
    public void performDoesNothingWhenAtEnd() {
        // Arrange
        final Position end = new Position(3, 3);

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(end);

        final IAction task = ActionFactory.createMoveTo(end);

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

        final IAction task = ActionFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        final boolean completed = task.isCompleted(performer);

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

        final IAction task = ActionFactory.createMoveTo(end);

        // Act
        task.perform(performer);
        final boolean completed = task.isCompleted(performer);

        // Assert
        assertThat(completed).isFalse();
    }

}
