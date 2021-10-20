package com.thebois.models.beings.roles;

import java.util.Optional;
import java.util.Random;

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
import com.thebois.models.world.IWorld;
import com.thebois.models.world.structures.IStructure;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuilderRoleTests {

    private IWorld world;
    private IPathFinder pathFinder;
    private IStructureFinder structureFinder;
    private AbstractRole role;
    private IActionPerformer performer;

    @BeforeEach
    public void setup() {
        world = mock(IWorld.class);
        RoleFactory.setWorld(world);
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        structureFinder = mock(IStructureFinder.class);
        RoleFactory.setStructureFinder(structureFinder);
        pathFinder = mock(IPathFinder.class);
        ActionFactory.setPathFinder(pathFinder);

        role = RoleFactory.builder();
        performer = mock(IActionPerformer.class);
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        RoleFactory.setStructureFinder(null);
        ActionFactory.setPathFinder(null);
    }

    @Test
    public void obtainNextActionReturnsDoNothingWhenAllStructuresAreComplete() {
        // Arrange
        final Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(anyInt())).thenReturn(5);

        final IStructure completeStructure1 = mock(IStructure.class);
        when(completeStructure1.getPosition()).thenReturn(new Position(10f, 20f));
        when(completeStructure1.isCompleted()).thenReturn(true);

        final IStructure completeStructure2 = mock(IStructure.class);
        when(completeStructure2.getPosition()).thenReturn(new Position(0f, 12f));
        when(completeStructure2.isCompleted()).thenReturn(true);

        when(structureFinder.getNearbyIncompleteStructure(any())).thenReturn(Optional.empty());

        final IAction doNothing = ActionFactory.createDoNothing();

        // Act
        final IAction actualAction = role.obtainNextAction(performer);

        // Assert
        assertThat(actualAction).isEqualTo(doNothing);
    }

    @Test
    public void obtainNextActionReturnsMoveToNeighborPositionOfNearestIncompleteStructure() {
        // Arrange
        final Position performerPosition = new Position(0, 0);
        when(performer.getPosition()).thenReturn(performerPosition);

        final Position structurePosition = new Position(5, 3);
        final Position besidesPosition = structurePosition.subtract(1, 0);
        final IStructure structure = MockFactory.createStructure(structurePosition, false);
        when(world.getClosestNeighbourOf(structure,
                                         performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));

        when(structureFinder.getNearbyIncompleteStructure(performerPosition)).thenReturn(Optional.of(
            structure));

        final IAction expectedAction = ActionFactory.createMoveTo(besidesPosition);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    @Test
    public void obtainNextActionReturnsDoNothingWhenNearestIncompleteStructureHasNoVacantNeighbours() {
        // Arrange
        final Position performerPosition = new Position(0, 0);
        when(performer.getPosition()).thenReturn(performerPosition);

        final Position structurePosition = new Position(5, 3);
        final IStructure structure = MockFactory.createStructure(structurePosition, false);
        when(world.getClosestNeighbourOf(structure,
                                         performer.getPosition())).thenReturn(Optional.empty());

        when(structureFinder.getNearbyIncompleteStructure(performerPosition)).thenReturn(Optional.of(
            structure));

        final IAction expectedAction = ActionFactory.createDoNothing();

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    @Test
    public void obtainNextActionReturnsBuildWhenNextToStructure() {
        // Arrange
        final Position structurePosition = new Position(5, 3);
        final Position besidesPosition = structurePosition.subtract(1, 0);
        when(performer.getPosition()).thenReturn(besidesPosition);
        final IStructure structure = MockFactory.createStructure(structurePosition, false);
        when(world.getClosestNeighbourOf(structure,
                                         performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));

        when(structureFinder.getNearbyIncompleteStructure(besidesPosition)).thenReturn(Optional.of(
            structure));

        final IAction expectedAction = ActionFactory.createBuild(structure);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    @Test
    public void obtainNextActionReturnsDoNothingWhenNextToStructureAndStructureIsNoLongerIncomplete() {
        // Arrange
        final Position structurePosition = new Position(5, 3);
        final Position besidesPosition = structurePosition.subtract(1, 0);
        when(performer.getPosition()).thenReturn(besidesPosition);
        final IStructure structure = MockFactory.createStructure(structurePosition, false);
        when(world.getClosestNeighbourOf(structure,
                                         performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));

        when(structureFinder.getNearbyIncompleteStructure(besidesPosition))
            .thenReturn(Optional.of(structure))
            .thenReturn(Optional.empty());

        final IAction expectedAction = ActionFactory.createDoNothing();

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

}
