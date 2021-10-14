package com.thebois.models.beings.actions;

import org.junit.jupiter.api.Test;

import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.resources.IResource;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HarvestActionTests {

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

}
