package com.thebois.models.beings.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.IReceiver;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class GiveItemActionTests {

    private IActionPerformer performer;
    private IAction action;
    private IReceiver receiver;
    private ItemType itemType;

    @BeforeEach
    public void setup() {
        itemType = ItemType.ROCK;
        receiver = mock(IReceiver.class);
        performer = mock(IActionPerformer.class);
        action = ActionFactory.createGiveItem(receiver, itemType);
    }

    @Test
    public void receiverGetsItemIfPerformerHaveItem() {
        // Arrange
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        action.perform(performer);

        // Assert
        verify(receiver, times(1)).addItem(any());
    }

    @Test
    public void receiverDoNoTGetItemIfPerformerDoNotHaveItem() {
        // Arrange
        when(performer.hasItem(any())).thenReturn(false);

        // Act
        action.perform(performer);

        // Assert
        verify(receiver, times(0)).addItem(any());
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
        when(receiver.getPosition()).thenReturn(position);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isTrue();
    }

    @Test
    public void canNotPerformIfPerformerIsTooFarAwayFromReceiver() {
        // Arrange
        final Position positionPerformer = new Position(0, 0);
        final Position positionReceiver = new Position(5, 5);
        when(performer.getPosition()).thenReturn(positionPerformer);
        when(receiver.getPosition()).thenReturn(positionReceiver);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isFalse();
    }

}
