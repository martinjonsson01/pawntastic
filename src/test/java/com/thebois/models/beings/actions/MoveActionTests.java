package com.thebois.models.beings.actions;

import java.io.IOException;
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
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.testutils.InMemorySerialize;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoveActionTests {

    private IPathFinder pathFinder;
    private Position start;
    private Position end;
    private List<Position> workingPath;
    private List<Position> noPath;
    private IActionPerformer performer;
    private IAction action;

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

        start = new Position(0, 0);
        end = new Position(3, 3);
        workingPath = List.of(new Position(1, 1), new Position(2, 2), end);
        noPath = List.of();
        performer = mock(IActionPerformer.class);
        action = ActionFactory.createMoveTo(end);
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
    public void canPerformReturnsFalseWhenDestinationIsUnreachable() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);
        when(pathFinder.path(start, end)).thenReturn(noPath);

        // Act
        action.perform(performer);
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void canPerformIsTrueWhenDestinationIsReachable() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);
        when(pathFinder.path(start, end)).thenReturn(workingPath);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isTrue();
    }

    @Test
    public void canPerformIsFalseWhenDestinationIsUnreachable() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);
        when(pathFinder.path(start, end)).thenReturn(noPath);

        // Act
        action.perform(performer);
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
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
        final Position begin, final Position destination) {
        // Arrange
        when(performer.getPosition()).thenReturn(begin);
        action = ActionFactory.createMoveTo(destination);

        final List<Position> path = List.of(new Position(1, 1), new Position(2, 2), destination);
        when(pathFinder.path(begin, destination)).thenReturn(path);

        final ArgumentCaptor<Position> destinationCaptor = ArgumentCaptor.forClass(Position.class);

        // Act
        for (final Position pathPosition : path) {
            action.perform(performer);
            // Simulate moving to path position.
            when(performer.getPosition()).thenReturn(pathPosition);
        }

        // Assert
        verify(performer, times(path.size())).setDestination(destinationCaptor.capture());
        final List<Position> destinations = destinationCaptor.getAllValues();
        assertThat(destinations).containsExactlyElementsOf(path);
    }

    @Test
    public void canPerformIsFalseWhenNewObstacleBlocksAllPossiblePathsToDestination() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);

        when(pathFinder.path(any(), any())).thenReturn(workingPath).thenReturn(noPath);

        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(1, 1);

        // Act
        action.perform(performer);
        Pawntastic.getEventBus().post(obstacleEvent);
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void entirePathIsRecalculatedWhenObstacleIsInWayAtStartOfPath() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);
        when(pathFinder.path(any(), any())).thenReturn(workingPath);

        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(1, 1);

        // Act
        action.perform(performer);
        Pawntastic.getEventBus().post(obstacleEvent);

        // Assert
        verify(pathFinder, times(2)).path(any(), any());
    }

    @Test
    public void pathIsRecalculatedFromCurrentPositionWhenPerformerNotNearStartOfPath() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);
        when(pathFinder.path(any(), any())).thenReturn(workingPath);

        // Calculate initial path.
        action.perform(performer);

        // Simulate performer moving away from start of path.
        final Position farAwayFromStart = start.add(10, 10);
        when(performer.getPosition()).thenReturn(farAwayFromStart);

        // Act
        action.perform(performer);

        // Assert
        verify(pathFinder, times(1)).path(eq(farAwayFromStart), eq(end));
    }

    @Test
    public void pathIsNotRecalculatedWhenObstacleIsPlacedAndThereIsNoPath() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);
        when(pathFinder.path(any(), any())).thenReturn(noPath);

        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(1, 1);

        // Act
        action.perform(performer);
        Pawntastic.getEventBus().post(obstacleEvent);

        // Assert
        verify(pathFinder, times(1)).path(any(), any());
    }

    @Test
    public void performDoesNothingWhenAtEnd() {
        // Arrange
        when(performer.getPosition()).thenReturn(end);

        // Act
        action.perform(performer);

        // Assert
        verify(performer, times(0)).setDestination(any());
    }

    @Test
    public void isCompletedIsTrueWhenAtEnd() {
        // Arrange
        when(performer.getPosition()).thenReturn(end);

        // Act
        action.perform(performer);
        final boolean completed = action.isCompleted(performer);

        // Assert
        assertThat(completed).isTrue();
    }

    @Test
    public void isCompletedIsFalseWhenAtStart() {
        // Arrange
        when(performer.getPosition()).thenReturn(start);
        when(pathFinder.path(start, end)).thenReturn(workingPath);

        // Act
        action.perform(performer);
        final boolean completed = action.isCompleted(performer);

        // Assert
        assertThat(completed).isFalse();
    }

    @Test
    public void sameObjectAfterDeserialization() throws ClassNotFoundException, IOException {
        // Act
        final byte[] serializedAction = InMemorySerialize.serialize(action);
        final IAction deserializedAction =
            (IAction) InMemorySerialize.deserialize(serializedAction);

        // Assert
        assertThat(action).isEqualTo(deserializedAction);
    }

}
