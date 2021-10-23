package com.thebois.models.beings.actions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TakeItemActionTests {

    private IActionPerformer performer;
    private IAction action;
    private ITakeable takeable;
    private Position position = new Position(0, 0);
    final ItemType itemType = ItemType.ROCK;

    @BeforeEach
    public void setup() {
        takeable = mock(ITakeable.class);
        performer = mock(IActionPerformer.class);
        action = ActionFactory.createTakeItem(takeable, itemType, position);
    }

    @Test
    public void performerTakesItemIfTakeableHasItem() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(true);
        when(takeable.take(any())).thenReturn(ItemFactory.fromType(itemType));
        when(performer.canFitItem(any())).thenReturn(true);

        // Act
        action.perform(performer, 5f);

        // Assert
        verify(takeable, times(1)).take(any());
    }

    @Test
    public void performerDoNotTakesItemIfPerformerCanNotFitItem() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(true);
        when(takeable.take(any())).thenReturn(ItemFactory.fromType(itemType));
        when(performer.canFitItem(any())).thenReturn(false);

        // Act
        action.perform(performer, 5f);

        // Assert
        verify(takeable, times(0)).take(any());
    }

    @Test
    public void performerDoesNotTakeItemIfTakableDoesNotHaveItem() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(false);

        // Act
        action.perform(performer, 2f);

        // Assert
        verify(takeable, times(0)).take(any());
    }

    @Test
    public void isNotCompletedIfPerformerStillNeedMoreOfItemType() {
        // Arrange
        action = ActionFactory.createTakeItem(takeable, itemType, position);
        when(takeable.hasItem(itemType)).thenReturn(true);

        // Act
        final boolean isCompleted = action.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isFalse();
    }

    @Test
    public void canPerformIfPerformerIsCloseEnoughToTakeable() {
        // Arrange
        final Position position = new Position(0, 0);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isTrue();
    }

    @Test
    public void canNotPerformIfPerformerIsTooFarAwayFromTakeable() {
        // Arrange
        final Position position = this.position.add(4, 0);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isFalse();
    }

}
