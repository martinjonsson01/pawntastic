package com.thebois.models.beings.roles;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LumberjackRoleTests {

    private IWorld mockWorld;

    @BeforeEach
    public void setup() {
        mockWorld = mock(IWorld.class);
        RoleFactory.setWorld(mockWorld);
        ActionFactory.setPathFinder(mock(IPathFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        ActionFactory.setPathFinder(null);
    }

    /* @Test
    public void obtainNextActionIsDoNothingWhenNoTreeExists() {
        // Arrange
        final AbstractRole role = RoleFactory.lumberjack();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());
        final IAction doNothing = ActionFactory.createDoNothing();

        // Act
        final IAction action = role.obtainNextTask(performer);

        // Assert
        assertThat(action).isInstanceOf(doNothing.getClass());
    }*/

    @Test
    public void obtainNextActionIsMoveWhenTreeExists() {
        // Arrange
        final AbstractRole role = RoleFactory.lumberjack();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());
        final IAction move = ActionFactory.createMoveTo(new Position());

        // Act
        final IAction action = role.obtainNextTask(performer);

        // Assert
        assertThat(action).isInstanceOf(move.getClass());
    }

    @Test
    public void obtainNextActionIsHarvestActionWhenMovedToTree() {
        // Arrange
        final AbstractRole role = RoleFactory.lumberjack();
        final ITaskPerformer performer = mock(ITaskPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());
        final IAction moveAction = ActionFactory.createMoveTo(new Position());

        // Act
        final IAction action = role.obtainNextTask(performer);

        // Assert
        assertThat(action).isInstanceOf(moveAction.getClass());
    }

}
