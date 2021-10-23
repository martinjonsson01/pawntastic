package com.thebois.models.beings.actions;

import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.ITakeable;
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

    public static Stream<Arguments> getEqualTakeItems() {
        final ITakeable sameTakeable = mock(ITakeable.class);
        final Position samePosition = new Position(0, 0);
        final ItemType sameItemType = ItemType.LOG;
        final IAction sameInstance =
            ActionFactory.createTakeItem(sameTakeable, sameItemType, samePosition);
        return Stream.of(Arguments.of(sameInstance, sameInstance),
                         Arguments.of(ActionFactory.createTakeItem(sameTakeable,
                                                                   sameItemType,
                                                                   samePosition),
                                      ActionFactory.createTakeItem(sameTakeable,
                                                                   sameItemType,
                                                                   samePosition)));
    }

    public static Stream<Arguments> getNotEqualTakeItems() {
        final ITakeable sameTakeable = mock(ITakeable.class);
        final Position samePosition = new Position(0, 0);
        final ItemType sameItemType = ItemType.LOG;
        final ItemType otherItemType = ItemType.ROCK;

        final IAction sameTakeItemNotStarted =
            ActionFactory.createTakeItem(sameTakeable, sameItemType, samePosition);
        final IAction sameTakeITemStarted =
            ActionFactory.createTakeItem(sameTakeable, sameItemType, samePosition);
        sameTakeITemStarted.perform(mock(IActionPerformer.class), 0.1f);
        return Stream.of(Arguments.of(
                             ActionFactory.createTakeItem(mock(ITakeable.class), sameItemType, samePosition),
                             sameTakeItemNotStarted),
                         Arguments.of(ActionFactory.createTakeItem(sameTakeable,
                                                                   otherItemType,
                                                                   samePosition),
                                      sameTakeItemNotStarted),
                         Arguments.of(ActionFactory.createTakeItem(sameTakeable,
                                                                   sameItemType,
                                                                   samePosition.subtract(1, 1)),
                                      sameTakeItemNotStarted),
                         Arguments.of(sameTakeItemNotStarted, null),
                         Arguments.of(sameTakeItemNotStarted, mock(IAction.class)),
                         Arguments.of(sameTakeItemNotStarted, sameTakeITemStarted));
    }

    @BeforeEach
    public void setup() {
        takeable = mock(ITakeable.class);
        performer = mock(IActionPerformer.class);
        action = ActionFactory.createTakeItem(takeable, itemType, position);
    }

    @Test
    public void performTakesItemFromTakeableAndGivesToPerformer() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(true);
        when(takeable.take(any())).thenReturn(ItemFactory.fromType(itemType));
        when(performer.canFitItem(any())).thenReturn(true);
        when(performer.getPosition()).thenReturn(position);

        // Act
        action.perform(performer, 5f);

        // Assert
        verify(takeable, times(1)).take(any());
        verify(performer, times(1)).tryAdd(any());
    }

    @Test
    public void canPerformIsFalseWhenPerformerCanNotFitItem() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(true);
        when(takeable.take(any())).thenReturn(ItemFactory.fromType(itemType));
        when(performer.canFitItem(any())).thenReturn(false);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void canPerformIsFalseWhenTakeableDoesNotHaveItem() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(false);
        when(takeable.take(any())).thenReturn(ItemFactory.fromType(itemType));
        when(performer.canFitItem(any())).thenReturn(true);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void canPerformIsFalseWhenPerformerNotCloseEnoughToTakeable() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(true);
        when(takeable.take(any())).thenReturn(ItemFactory.fromType(itemType));
        when(performer.canFitItem(any())).thenReturn(true);
        when(performer.getPosition()).thenReturn(position.add(5, 5));

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void canPerformIsTrueWhenPerformerIsCloseEnoughHasSpaceAndTakeableHasItem() {
        // Arrange
        when(takeable.hasItem(any())).thenReturn(true);
        when(takeable.take(any())).thenReturn(ItemFactory.fromType(itemType));
        when(performer.canFitItem(any())).thenReturn(true);
        when(performer.getPosition()).thenReturn(position);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getEqualTakeItems")
    public void equalsIsTrueForEqualGiveItem(final IAction first, final IAction second) {
        // Assert
        Assertions.assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualTakeItems")
    public void equalsIsFalseForNotEqualGiveItem(final IAction first, final IAction second) {
        // Assert
        Assertions.assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void hashCodeIsSameForEqualActions() {
        // Arrange
        final IAction first = ActionFactory.createTakeItem(takeable, itemType, position);
        final IAction second = ActionFactory.createTakeItem(takeable, itemType, position);

        // Assert
        Assertions.assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

}
