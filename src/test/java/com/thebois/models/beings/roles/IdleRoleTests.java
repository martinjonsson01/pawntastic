package com.thebois.models.beings.roles;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.tasks.ITask;
import com.thebois.models.beings.tasks.TaskFactory;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IdleRoleTests {

    @BeforeEach
    public void setup() {
        TaskFactory.setPathFinder(mock(IPathFinder.class));
    }

    @AfterEach
    public void teardown() {
        TaskFactory.setPathFinder(null);
    }

    @Test
    public void obtainNextTaskIsMoveTaskToRandomPositionInWorld() {
        // Arrange
        final IWorld mockWorld = mock(IWorld.class);
        final ITile mockTile = mock(ITile.class);
        final Position randomPosition = new Position(2, 3);
        when(mockTile.getPosition()).thenReturn(randomPosition);
        when(mockWorld.getRandomVacantSpot()).thenReturn(mockTile);
        final AbstractRole role = RoleFactory.idle(mockWorld);
        final ITask expectedTask = TaskFactory.createMoveTo(randomPosition);

        // Act
        final ITask task = role.obtainNextTask();

        // Assert
        assertThat(task).isEqualTo(expectedTask);
    }

    @Test
    public void roleWithoutWorldAlwaysMovesToOrigin() {
        // Arrange
        final AbstractRole role = RoleFactory.idle();
        final ITask expectedTask = TaskFactory.createMoveTo(new Position(0, 0));

        // Act
        final ITask task = role.obtainNextTask();

        // Assert
        assertThat(task).isEqualTo(expectedTask);
    }

}
