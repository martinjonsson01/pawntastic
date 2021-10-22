package com.thebois.models.beings.actions;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.resources.IResource;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HarvestActionTests {

    private IActionPerformer performer;
    private IResource resource;
    private IAction action;
    private IItem resourceItem;

    public static Stream<Arguments> getEqualHarvests() {
        final IResource sameResource = mock(IResource.class);
        final IAction sameInstance = ActionFactory.createHarvest(sameResource);
        return Stream.of(Arguments.of(sameInstance, sameInstance),
                         Arguments.of(ActionFactory.createHarvest(sameResource),
                                      ActionFactory.createHarvest(sameResource)));
    }

    public static Stream<Arguments> getNotEqualHarvests() {
        final IResource sameResource = mock(IResource.class);
        final IAction sameResourceNotHarvested = ActionFactory.createHarvest(sameResource);
        final IAction sameResourceHarvested = ActionFactory.createHarvest(sameResource);
        sameResourceHarvested.perform(mock(IActionPerformer.class), 0.1f);
        return Stream.of(Arguments.of(ActionFactory.createHarvest(mock(IResource.class)),
                                      ActionFactory.createHarvest(sameResource)),
                         Arguments.of(ActionFactory.createHarvest(sameResource), null),
                         Arguments.of(ActionFactory.createHarvest(sameResource),
                                      mock(IAction.class)),
                         Arguments.of(sameResourceNotHarvested, sameResourceHarvested));
    }

    @BeforeEach
    public void setup() {
        performer = mock(IActionPerformer.class);
        resourceItem = mock(IItem.class);
        resource = MockFactory.createResource(new Position(1, 0), resourceItem, 10f);
        action = ActionFactory.createHarvest(resource);
    }

    @Test
    public void canPerformReturnsFalseWhenFarAwayFromResource() {
        // Arrange
        when(performer.getPosition()).thenReturn(new Position(10, 10));

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isFalse();
    }

    @Test
    public void canPerformReturnsTrueWhenNextToResource() {
        // Arrange
        final Position besidesResource = resource.getPosition().subtract(1, 0);
        when(performer.getPosition()).thenReturn(besidesResource);

        // Act
        final boolean canPerform = action.canPerform(performer);

        // Assert
        assertThat(canPerform).isTrue();
    }

    @Test
    public void performDoesNotAddHarvestedResourceToPerformerBeforeHarvestTimeHasPassed() {
        // Act
        for (int i = 0; i < 3; i++) {
            action.perform(performer, 0.1f);
        }

        // Assert
        verify(performer, times(0)).addItem(any());
    }

    @Test
    public void performAddsHarvestedResourceToPerformerAfterHarvestTimeHasPassed() {
        // Act
        action.perform(performer, resource.getHarvestTime());

        // Assert
        verify(performer, times(1)).addItem(eq(resourceItem));
    }

    @Test
    public void isCompletedIsFalseBeforePerforming() {
        // Act
        final boolean completed = action.isCompleted(performer);

        // Assert
        assertThat(completed).isFalse();
    }

    @Test
    public void isCompletedIsTrueAfterPerformingHarvestDuration() {
        action.perform(performer, resource.getHarvestTime());

        // Act
        final boolean completed = action.isCompleted(performer);

        // Assert
        assertThat(completed).isTrue();
    }

    @Test
    public void isCompletedIsTrueAfterPerformingMultipleTimesThatAddUpToHarvestDuration() {
        final int fractions = 10;
        final float timeSpentHarvesting = resource.getHarvestTime() / fractions;
        for (int i = 0; i < fractions; i++) {
            action.perform(performer, timeSpentHarvesting);
        }

        // Act
        final boolean completed = action.isCompleted(performer);

        // Assert
        assertThat(completed).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getEqualHarvests")
    public void equalsIsTrueForEqualHarvests(final IAction first, final IAction second) {
        // Assert
        assertThat(first).isEqualTo(second);
    }

    @ParameterizedTest
    @MethodSource("getNotEqualHarvests")
    public void equalsIsFalseForNotEqualHarvests(final IAction first, final IAction second) {
        // Assert
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    public void hashCodeIsSameForEqualActions() {
        // Arrange
        final IResource same = mock(IResource.class);
        final IAction first = ActionFactory.createHarvest(same);
        final IAction second = ActionFactory.createHarvest(same);

        // Assert
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

}
