package com.thebois.models.beings.actions;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.structures.IStructure;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuildActionTests {

    private static final float ITEM_TRANSFER_TIME = 2f;
    private IActionPerformer performer;
    private IStructure structure;
    private IAction action;
    private Position besidesStructure;
    private List<ItemType> neededItemTypes;

    public static Stream<Arguments> getEqualBuilds() {
        final IStructure sameStructure = MockFactory.createStructure(new Position(), false);
        final IAction sameInstance = ActionFactory.createBuild(sameStructure);
        return Stream.of(Arguments.of(sameInstance, sameInstance),
                         Arguments.of(ActionFactory.createBuild(sameStructure),
                                      ActionFactory.createBuild(sameStructure)));
    }

    public static Stream<Arguments> getNotEqualBuilds() {
        final IStructure sameStructure = MockFactory.createStructure(new Position(), false);
        return Stream.of(Arguments.of(ActionFactory.createBuild(mock(IStructure.class)),
                                      ActionFactory.createBuild(sameStructure)),
                         Arguments.of(ActionFactory.createBuild(sameStructure), null),
                         Arguments.of(ActionFactory.createBuild(sameStructure),
                                      mock(IAction.class)));
    }

    @BeforeEach
    public void setup() {
        structure = MockFactory.createStructure(new Position(10, 10), false);
        when(structure.tryDeliverItem(any())).thenReturn(true);
        neededItemTypes = List.of(ItemType.LOG, ItemType.LOG, ItemType.ROCK);
        when(structure.getNeededItems()).thenReturn(neededItemTypes);

        performer = mock(IActionPerformer.class);
        action = ActionFactory.createBuild(structure);

        besidesStructure = structure.getPosition().subtract(1, 0);
    }

    @Test
    public void performDeliversASingleNeededItemToStructureWhenTimeIsAtItemTransferTime() {
        // Arrange
        when(performer.getPosition()).thenReturn(besidesStructure);
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        action.perform(performer, ITEM_TRANSFER_TIME);

        // Assert
        final ArgumentCaptor<IItem> itemCaptor = ArgumentCaptor.forClass(IItem.class);
        verify(structure, atLeastOnce()).tryDeliverItem(itemCaptor.capture());
    }

    @Test
    public void performDeliversASingleNeededItemToStructureWhenMultiplePerformsAddUpToTransferTime() {
        // Arrange
        final Position besidesStructure = structure.getPosition().subtract(1, 0);
        when(performer.getPosition()).thenReturn(besidesStructure);
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        final int fractions = 10;
        final float timeSpentDelivering = ITEM_TRANSFER_TIME / fractions;
        for (int i = 0; i < fractions; i++) {
            action.perform(performer, timeSpentDelivering);
        }

        // Assert
        final ArgumentCaptor<IItem> itemCaptor = ArgumentCaptor.forClass(IItem.class);
        verify(structure, atLeastOnce()).tryDeliverItem(itemCaptor.capture());
    }

    @Test
    public void performDeliversNothingWhenTimeBelowItemTransferTime() {
        // Arrange
        when(performer.getPosition()).thenReturn(besidesStructure);

        // Act
        action.perform(performer, 0.1f);

        // Assert
        verify(structure, times(0)).tryDeliverItem(any());
    }
    
    @Test
    public void isCompletedReturnsFalseWhenPerformedForLessThanItemTransferTime() {
        // Arrange
        when(performer.getPosition()).thenReturn(besidesStructure);

        action.perform(performer, 0.1f);

        // Act
        final boolean isCompleted = action.isCompleted(performer);

        // Assert
        assertThat(isCompleted).isFalse();
    }

    @Test
    public void canPerformReturnsTrueWhenNearbyAndHasNeededItem() {
        // Arrange
        when(performer.getPosition()).thenReturn(besidesStructure);
        when(performer.hasItem(any())).thenReturn(true);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isTrue();
    }

    @Test
    public void canPerformReturnsFalseWhenFarAway() {
        // Arrange
        final Position awayFromStructure = structure.getPosition().add(10, 10);
        when(performer.getPosition()).thenReturn(awayFromStructure);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void canPerformReturnsFalseWhenPerformerDoesNotHaveNeededItem() {
        // Arrange
        when(performer.getPosition()).thenReturn(besidesStructure);
        when(performer.hasItem(any())).thenReturn(false);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getEqualBuilds")
    public void equalsIsTrueForEquals(final IAction first, final IAction second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualBuilds")
    public void equalsIsFalseForNotEquals(final IAction first, final IAction second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

}
