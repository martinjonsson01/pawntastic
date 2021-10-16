package com.thebois.models.beings.roles;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FarmerRoleTests {

    private IWorld world;

    @BeforeEach
    public void setup() {
        world = mock(IWorld.class);
        RoleFactory.setWorld(world);
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        ActionFactory.setPathFinder(mock(IPathFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        ActionFactory.setPathFinder(null);
    }

    @Test
    public void obtainNextActionReturnsSameAsIdleRole() {
        // Arrange
        final IActionPerformer performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));

        final ITile tile = MockFactory.createTile(10, 10);
        when(world.getRandomVacantSpot()).thenReturn(tile);

        final AbstractRole role = RoleFactory.farmer();
        final AbstractRole idleRole = RoleFactory.idle();

        final IAction expectedAction = idleRole.obtainNextAction(performer);

        // Act
        final IAction actualAction = role.obtainNextAction(performer);

        // Assert
        assertThat(actualAction).isEqualTo(expectedAction);
    }

}
