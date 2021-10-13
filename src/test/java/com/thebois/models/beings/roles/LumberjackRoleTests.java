package com.thebois.models.beings.roles;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LumberjackRoleTests {

    private IWorld mockWorld;
    private IResourceFinder finder;

    @BeforeEach
    public void setup() {
        mockWorld = mock(IWorld.class);
        ActionFactory.setPathFinder(mock(IPathFinder.class));
        RoleFactory.setWorld(mockWorld);
        finder = mock(IResourceFinder.class);
        RoleFactory.setResourceFinder(finder);
    }

    @AfterEach
    public void teardown() {
        ActionFactory.setPathFinder(null);
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
    }

    @Test
    public void obtainNextActionIsDoNothingWhenNoTreeExists() {
        // Arrange
        final AbstractRole role = RoleFactory.lumberjack();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE))).thenReturn(Optional.empty());
        final IAction expectedAction = ActionFactory.createDoNothing();

        // Act
        final IAction actual = role.obtainNextTask(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    @Test
    public void obtainNextActionIsMoveToTreeWhenTreeExists() {
        // Arrange
        final AbstractRole role = RoleFactory.lumberjack();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());
        final Position treePosition = new Position(5, 3);
        mockTree(treePosition);
        final IAction expectedAction = ActionFactory.createMoveTo(treePosition);

        // Act
        final IAction actual = role.obtainNextTask(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    private void mockTree(final Position treePosition) {
        final IResource tree = mockResource(treePosition);
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE))).thenReturn(Optional.of(tree));
    }

    private IResource mockResource(final Position position) {
        final IResource resource = mock(IResource.class);
        when(resource.getPosition()).thenReturn(position);
        return resource;
    }

}
