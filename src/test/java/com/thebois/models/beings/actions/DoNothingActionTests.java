package com.thebois.models.beings.actions;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoNothingActionTests {

    @Test
    public void performDoesNotSetDestination() {
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());

        final IAction doNothing = ActionFactory.createDoNothing();

        // Act
        doNothing.perform(performer);

        // Assert
        verify(performer, times(0)).setDestination(any());
    }

    @Test
    public void isCompletedReturnsFalse() {
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());

        final IAction doNothing = ActionFactory.createDoNothing();

        // Act
        doNothing.perform(performer);
        final boolean isCompleted = doNothing.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isFalse();
    }

    @Test
    public void doNothingActionIsEqualToOtherInstanceOfDoNothing() {
        // Arrange
        final IAction firstInstance = ActionFactory.createDoNothing();
        final IAction secondInstance = ActionFactory.createDoNothing();

        // Assert
        assertThat(firstInstance).isEqualTo(secondInstance);
    }

    @Test
    public void doNothingActionIsEqualToItself() {
        // Arrange
        final IAction firstInstance = ActionFactory.createDoNothing();

        // Assert
        assertThat(firstInstance).isEqualTo(firstInstance);
    }

    @Test
    public void doNothingActionHasSameHashCodeAsOtherInstanceOfDoNothing() {
        // Arrange
        final IAction firstInstance = ActionFactory.createDoNothing();
        final IAction secondInstance = ActionFactory.createDoNothing();

        // Assert
        assertThat(firstInstance.hashCode()).isEqualTo(secondInstance.hashCode());
    }

}
