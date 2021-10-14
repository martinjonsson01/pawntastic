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

public class HarvesterRoleTests {

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
        final AbstractRole role = createRole();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE))).thenReturn(Optional.empty());
        final IAction expectedAction = ActionFactory.createDoNothing();

        // Act
        final IAction actual = role.obtainNextTask(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    private AbstractRole createRole() {
        return new TestHarvesterRole(finder, mockWorld);
    }

    @Test
    public void obtainNextActionIsMoveToBesidesTreeWhenTreeExists() {
        // Arrange
        final AbstractRole role = createRole();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));
        final Position treePosition = new Position(5, 3);
        final Position besidesPosition = new Position(4, 3);
        final IResource tree = mockTree(treePosition);
        when(mockWorld.getClosestNeighbourOf(tree, performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));
        final IAction expectedAction = ActionFactory.createMoveTo(besidesPosition);

        // Act
        final IAction actual = role.obtainNextTask(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    private IResource mockTree(final Position treePosition) {
        final IResource tree = mockResource(treePosition);
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE))).thenReturn(Optional.of(tree));
        return tree;
    }

    private IResource mockResource(final Position position) {
        final IResource resource = mock(IResource.class);
        when(resource.getPosition()).thenReturn(position);
        return resource;
    }

    @Test
    public void obtainNextActionIsDoNothingWhenTreeExistsButHasNoVacantNeighbours() {
        // Arrange
        final AbstractRole role = createRole();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));
        final Position treePosition = new Position(5, 3);
        final IResource tree = mockTree(treePosition);
        when(mockWorld.getClosestNeighbourOf(tree,
                                             performer.getPosition())).thenReturn(Optional.empty());
        final IAction expectedAction = ActionFactory.createDoNothing();

        // Act
        final IAction actual = role.obtainNextTask(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    @Test
    public void obtainNextActionIsHarvestWhenPerformerNextToTree() {
        // Arrange
        final AbstractRole role = createRole();

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        final Position treePosition = new Position(5, 3);
        final Position besidesPosition = new Position(4, 3);
        when(performer.getPosition()).thenReturn(besidesPosition);

        final IResource tree = mockTree(treePosition);
        when(mockWorld.getClosestNeighbourOf(tree, performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));

        final IAction expected = ActionFactory.createHarvest(tree);

        // Act
        final IAction actual = role.obtainNextTask(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionIsDoNothingWhenPerformerNextToTreeButTreeIsGone() {
        // Arrange
        final AbstractRole role = createRole();

        final ITaskPerformer performer = mock(ITaskPerformer.class);
        final Position treePosition = new Position(5, 3);
        final Position besidesPosition = new Position(4, 3);
        when(performer.getPosition()).thenReturn(besidesPosition);

        final IResource tree = mockTree(treePosition);
        when(mockWorld.getClosestNeighbourOf(tree, performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));

        // Simulate tree getting removed by only returning it the first time.
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE)))
            .thenReturn(Optional.of(tree))
            .thenReturn(Optional.empty());

        final IAction expected = ActionFactory.createDoNothing();

        // Act
        final IAction actual = role.obtainNextTask(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * A harvester role stub-implementation that harvests trees, much like a lumberjack.
     */
    private static final class TestHarvesterRole extends AbstractHarvesterRole {

        TestHarvesterRole(final IResourceFinder finder, final IWorld world) {
            super(finder, world, ResourceType.TREE);
        }

        @Override
        public RoleType getType() {
            return null;
        }

    }

}
