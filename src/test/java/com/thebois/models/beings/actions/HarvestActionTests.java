package com.thebois.models.beings.actions;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.resources.IResource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HarvestActionTests {

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
        sameResourceHarvested.perform(mock(ITaskPerformer.class));
        return Stream.of(Arguments.of(ActionFactory.createHarvest(mock(IResource.class)),
                                      ActionFactory.createHarvest(sameResource)),
                         Arguments.of(ActionFactory.createHarvest(sameResource), null),
                         Arguments.of(ActionFactory.createHarvest(sameResource),
                                      mock(IAction.class)),
                         Arguments.of(sameResourceNotHarvested, sameResourceHarvested));
    }

    @Test
    public void performAddsHarvestedResourceToPerformer() {
        // Arrange
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        final IResource resource = mock(IResource.class);
        final IItem resourceItem = mock(IItem.class);
        when(resource.harvest()).thenReturn(resourceItem);
        final IAction action = ActionFactory.createHarvest(resource);

        // Act
        action.perform(performer);

        // Assert
        verify(performer, times(1)).addItem(eq(resourceItem));
    }

    @Test
    public void isCompletedIsFalseBeforePerforming() {
        // Arrange
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        final IResource resource = mock(IResource.class);
        final IItem resourceItem = mock(IItem.class);
        when(resource.harvest()).thenReturn(resourceItem);
        final IAction action = ActionFactory.createHarvest(resource);

        // Act
        final boolean completed = action.isCompleted(performer);

        // Assert
        assertThat(completed).isFalse();
    }

    @Test
    public void isCompletedIsFalseAfterPerforming() {
        // Arrange
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        final IResource resource = mock(IResource.class);
        final IItem resourceItem = mock(IItem.class);
        when(resource.harvest()).thenReturn(resourceItem);
        final IAction action = ActionFactory.createHarvest(resource);

        action.perform(performer);

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
