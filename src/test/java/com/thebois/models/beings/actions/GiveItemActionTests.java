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
        action = ActionFactory.createGiveItem(storeable, itemType, position);
    }

    @Test
    public void receiverGetsItemIfPerformerHaveItem() {
        // Arrange
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        action.perform(performer);

        // Assert
        verify(storeable, times(1)).tryAdd(any());
    }

    @Test
    public void receiverDoNoTGetItemIfPerformerDoNotHaveItem() {
        // Arrange
        when(performer.hasItem(any())).thenReturn(false);

        // Act
        action.perform(performer);

        // Assert
        verify(storeable, times(0)).tryAdd(any());
    }

    @Test
    public void isCompletedIfPerformerHasNoMoreItemsOfItemType() {
        // Arrange
        when(performer.hasItem(any())).thenReturn(false);

        // Act
        final boolean isCompleted = action.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isTrue();
    }

    @Test
    public void isNotCompletedIfPerformerHasMoreItemsOfItemType() {
        // Arrange
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        final boolean isCompleted = action.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isFalse();
    }

    @Test
    public void canPerformIfPerformerIsCloseEnoughToReceiver() {
        // Arrange
        final Position position = new Position(0, 0);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isTrue();
    }

    @Test
    public void canNotPerformIfPerformerIsTooFarAwayFromReceiver() {
        // Arrange
        final Position position = this.position.add(4, 0);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isFalse();
    }

}
