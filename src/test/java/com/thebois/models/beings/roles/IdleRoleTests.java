package com.thebois.models.beings.roles;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IdleRoleTests {

    private IWorld mockWorld;

    @BeforeEach
    public void setup() {
        mockWorld = mock(IWorld.class);
        RoleFactory.setWorld(mockWorld);
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        RoleFactory.setStructureFinder(mock(IStructureFinder.class));
        ActionFactory.setPathFinder(mock(IPathFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        RoleFactory.setStructureFinder(null);
        ActionFactory.setPathFinder(null);
    }

    @Test
    public void obtainNextTaskIsMoveTaskToRandomPositionInWorld() {
        // Arrange
        final ITile mockTile = mock(ITile.class);
        final Position randomPosition = new Position(2, 3);
        when(mockTile.getPosition()).thenReturn(randomPosition);
        when(mockWorld.getRandomVacantSpot()).thenReturn(mockTile);
        final AbstractRole role = RoleFactory.idle();
        final IAction expectedTask = ActionFactory.createMoveTo(randomPosition);
        final IActionPerformer performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());

        // Act
        final IAction task = role.obtainNextAction(performer);

        // Assert
        assertThat(task).isEqualTo(expectedTask);
    }

}
