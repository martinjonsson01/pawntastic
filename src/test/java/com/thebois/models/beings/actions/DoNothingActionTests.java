package com.thebois.models.beings.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoNothingActionTests {

    private IActionPerformer performer;
    private IAction doNothing;

    @Test
    public void isCompletedReturnsTrueAfterPerformedOnce() {
        // Arrange
        doNothing.perform(performer);

        // Act
        final boolean isCompleted = doNothing.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isTrue();
    }

    @BeforeEach
    public void setup() {
        performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());

        doNothing = ActionFactory.createDoNothing();
    }

    @Test
    public void performDoesNotSetDestination() {
        // Act
        doNothing.perform(performer);

        // Assert
        verify(performer, times(0)).setDestination(any());
    }

    @Test
    public void canPerformReturnsFalse() {
        // Act
        doNothing.perform(performer);
        final boolean canPerform = doNothing.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void isCompletedReturnsFalseWhenNotPerformed() {
        // Act
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
