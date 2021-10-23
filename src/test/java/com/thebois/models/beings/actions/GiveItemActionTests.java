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
import com.thebois.models.inventory.IStorable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class GiveItemActionTests {

    private IActionPerformer performer;
    private IAction action;
    private IStorable storable;
    private final Position position = new Position(0, 0);

    public static Stream<Arguments> getEqualGiveItems() {
        final IStorable sameStorable = mock(IStorable.class);
        final Position samePosition = new Position(0, 0);
        final IAction sameInstance = ActionFactory.createGiveItem(sameStorable, samePosition);
        return Stream.of(Arguments.of(sameInstance, sameInstance),
                         Arguments.of(ActionFactory.createGiveItem(sameStorable, samePosition),
                                      ActionFactory.createGiveItem(sameStorable, samePosition)));
    }

    public static Stream<Arguments> getNotEqualGiveItems() {
        final IStorable sameStorable = mock(IStorable.class);
        final Position samePosition = new Position(0, 0);

        final IAction sameGiveItemNotStarted =
            ActionFactory.createGiveItem(sameStorable, samePosition);
        final IAction sameGiveItemStarted =
            ActionFactory.createGiveItem(sameStorable, samePosition);
        sameGiveItemStarted.perform(mock(IActionPerformer.class), 0.1f);
        return Stream.of(Arguments.of(
                             ActionFactory.createGiveItem(mock(IStorable.class), samePosition),
                             sameGiveItemNotStarted),
                         Arguments.of(ActionFactory.createGiveItem(sameStorable,
                                                                   samePosition.subtract(1, 1)),
                                      sameGiveItemNotStarted),
                         Arguments.of(sameGiveItemNotStarted, null),
                         Arguments.of(sameGiveItemNotStarted, mock(IAction.class)),
                         Arguments.of(sameGiveItemNotStarted, sameGiveItemStarted));
    }

    @BeforeEach
    public void setup() {
        storable = mock(IStorable.class);
        performer = mock(IActionPerformer.class);
        action = ActionFactory.createGiveItem(storable, position);
    }

    @Test
    public void onPerformRemovesItemFromPerformerAndAddsItemToStorable() {
        // Act
        action.perform(performer, 2f);

        // Assert
        verify(storable, times(1)).tryAdd(any());
        verify(performer, times(1)).takeNextItem();
    }

    @Test
    public void canPerformIsTrueWhenPerformerIsCloseEnoughToStorableAndPerformerHasItem() {
        // Arrange
        when(performer.getPosition()).thenReturn(position);
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isTrue();
    }

    @Test
    public void canPerformIsFalseWhenPerformerIsTooFarAwayFromStorable() {
        // Arrange
        when(performer.getPosition()).thenReturn(position.add(5, 5));
        when(performer.isEmpty()).thenReturn(false);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isFalse();
    }

    @Test
    public void canPerformIsFalseWhenPerformerDoesNotHaveAnyItem() {
        // Arrange
        when(performer.getPosition()).thenReturn(position);
        when(performer.isEmpty()).thenReturn(true);

        // Act
        final boolean isCloseEnough = action.canPerform(performer);

        // Assert
        assertThat(isCloseEnough).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getEqualGiveItems")
    public void equalsIsTrueForEqualGiveItem(final IAction first, final IAction second) {
        // Assert
        Assertions.assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualGiveItems")
    public void equalsIsFalseForNotEqualGiveItem(final IAction first, final IAction second) {
        // Assert
        Assertions.assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void hashCodeIsSameForEqualActions() {
        // Arrange
        final IAction first = ActionFactory.createGiveItem(storable, position);
        final IAction second = ActionFactory.createGiveItem(storable, position);

        // Assert
        Assertions.assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

}
