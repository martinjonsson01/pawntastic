package com.thebois.models.beings.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoNextActionTests {

    private IActionPerformer performer;
    private IAction doNext;

    @BeforeEach
    public void setup() {
        performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());

        doNext = ActionFactory.createDoNext();
    }

    @Test
    public void canPerformReturnsFalse() {
        // Act
        doNext.perform(performer, 0.1f);
        final boolean canPerform = doNext.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void isCompletedReturnsTrue() {
        // Act
        final boolean isCompleted = doNext.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isTrue();
    }

    @Test
    public void doNextActionIsEqualToOtherInstanceOfDoNext() {
        // Arrange
        final IAction firstInstance = ActionFactory.createDoNext();
        final IAction secondInstance = ActionFactory.createDoNext();

        // Assert
        assertThat(firstInstance).isEqualTo(secondInstance);
    }

    @Test
    public void doNothingActionIsEqualToItself() {
        // Arrange
        final IAction firstInstance = ActionFactory.createDoNext();

        // Assert
        assertThat(firstInstance).isEqualTo(firstInstance);
    }

    @Test
    public void doNothingActionHasSameHashCodeAsOtherInstanceOfDoNothing() {
        // Arrange
        final IAction firstInstance = ActionFactory.createDoNext();
        final IAction secondInstance = ActionFactory.createDoNext();

        // Assert
        assertThat(firstInstance.hashCode()).isEqualTo(secondInstance.hashCode());
    }

}
