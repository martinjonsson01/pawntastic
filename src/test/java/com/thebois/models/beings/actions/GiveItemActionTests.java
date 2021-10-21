package com.thebois.models.beings.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class GiveItemActionTests {

    private IActionPerformer performer;
    private IAction action;
    private IStoreable storeable;
    private Position position = new Position(0, 0);

    @BeforeEach
    public void setup() {
        final ItemType itemType = ItemType.ROCK;
        storeable = mock(IStoreable.class);
        performer = mock(IActionPerformer.class);
        action = ActionFactory.createGiveItem(storeable, position);
    }

    @Test
    public void StorableGetsItemIfPerformerHaveItem() {
        // Arrange
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        action.perform(performer);

        // Assert
        verify(storeable, times(1)).tryAdd(any());
    }

    @Test
    public void StorableDoNoTGetItemIfPerformerDoNotHaveItem() {
        // Arrange
        when(performer.isEmpty()).thenReturn(true);

        // Act
        action.perform(performer);

        // Assert
        verify(storeable, times(0)).tryAdd(any());
    }

    @Test
    public void isCompletedIfPerformerHasNoMoreItems() {
        // Arrange
        when(performer.isEmpty()).thenReturn(true);

        // Act
        final boolean isCompleted = action.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isTrue();
    }

    @Test
    public void isNotCompletedIfPerformerHasMoreItems() {
        // Arrange
        when(performer.isEmpty()).thenReturn(false);

        // Act
        final boolean isCompleted = action.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isFalse();
    }

    @Test
    public void canPerformIfPerformerIsCloseEnoughToStorable() {
        // Arrange
        final Position position = new Position(0, 0);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isTrue();
    }

    @Test
    public void canNotPerformIfPerformerIsTooFarAwayFromStorable() {
        // Arrange
        final Position position = this.position.add(4, 0);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isFalse();
    }

}
