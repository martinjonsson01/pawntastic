package com.thebois.models.beings.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureFactory;
import com.thebois.models.world.structures.StructureType;
import com.thebois.testutils.MockFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HarvesterRoleTests {

    private IWorld mockWorld;
    private IResourceFinder finder;
    private IStructureFinder structureFinder;

    @BeforeEach
    public void setup() {
        mockWorld = mock(IWorld.class);
        ActionFactory.setPathFinder(mock(IPathFinder.class));
        RoleFactory.setWorld(mockWorld);
        finder = mock(IResourceFinder.class);
        RoleFactory.setResourceFinder(finder);
        structureFinder = mock(IStructureFinder.class);
        RoleFactory.setStructureFinder(structureFinder);

        final ITile mockTile = mock(ITile.class);
        final Position randomPosition = new Position(2, 3);
        when(mockTile.getPosition()).thenReturn(randomPosition);
        when(mockWorld.getRandomVacantSpotInRadiusOf(any(), anyInt())).thenReturn(mockTile);
    }

    @AfterEach
    public void teardown() {
        ActionFactory.setPathFinder(null);
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        RoleFactory.setStructureFinder(null);
    }

    @Test
    public void obtainNextActionIsIdleActionWhenNoTreeExists() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position());
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE))).thenReturn(Optional.empty());

        final IAction expectedAction = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    private AbstractRole createRole() {
        return new TestHarvesterRole(finder, structureFinder, mockWorld);
    }

    @Test
    public void obtainNextActionIsMoveToBesidesTreeWhenTreeExists() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));
        final Position treePosition = new Position(5, 3);
        final Position besidesPosition = treePosition.subtract(1, 0);
        final IResource tree = mockTree(treePosition);
        when(mockWorld.getClosestNeighbourOf(tree, performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));
        final IAction expectedAction = ActionFactory.createMoveTo(besidesPosition);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    private IResource mockTree(final Position treePosition) {
        final IResource tree = MockFactory.createResource(treePosition, mock(IItem.class), 10f);
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE))).thenReturn(Optional.of(tree));
        return tree;
    }

    private IResource mockResource(final Position position) {
        final IResource resource = mock(IResource.class);
        when(resource.getPosition()).thenReturn(position);
        when(resource.getType()).thenReturn(ResourceType.TREE);
        return resource;
    }

    @Test
    public void obtainNextActionIsIdleActionWhenTreeExistsButHasNoVacantNeighbours() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));
        final Position treePosition = new Position(5, 3);
        final IResource tree = mockTree(treePosition);
        when(mockWorld.getClosestNeighbourOf(tree,
                                             performer.getPosition())).thenReturn(Optional.empty());

        final IAction expectedAction = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    @Test
    public void obtainNextActionIsHarvestWhenPerformerNextToTreeAndHasInventorySpace() {
        // Arrange
        final AbstractRole role = createRole();

        final IActionPerformer performer = mock(IActionPerformer.class);
        final IResource tree = setUpRoleIsNextToTree(performer);

        final IAction expected = ActionFactory.createHarvest(tree);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionIsIdleActionWhenPerformerNextToTreeButTreeIsGone() {
        // Arrange
        final AbstractRole role = createRole();

        final IActionPerformer performer = mock(IActionPerformer.class);
        final IResource tree = setUpRoleIsNextToTree(performer);

        // Simulate tree getting removed by only returning it the first time.
        when(finder.getNearbyOfType(any(), eq(ResourceType.TREE))).thenReturn(Optional.of(tree))
                                                                  .thenReturn(Optional.empty());

        final IAction expectedAction = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expectedAction);
    }

    @Test
    public void obtainNextActionIdleWhenPerformerCanNotFindStockpile() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        // Set up performer
        final Position performerPosition = new Position(5, 5);
        when(performer.getPosition()).thenReturn(performerPosition);
        // Set up other positions
        final Position besidesPosition = performerPosition.subtract(1, 0);

        //Set up finder results
        setIWorldFinderResult(Optional.of(performerPosition), Optional.of(performerPosition));
        setResourceFinderResult(Optional.of(mockResource(besidesPosition)));
        setStructureFinderResult(Optional.empty());

        // Set up for expected action

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionIdleWhenPerformerCanNotFindEmptyPositionNextToStockpile() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        // Set up performer
        final Position performerPosition = new Position(5, 5);
        when(performer.getPosition()).thenReturn(performerPosition);
        // Set up other positions
        final Position besidesPosition = performerPosition.subtract(1, 0);

        //Set up finder results
        setIWorldFinderResult(Optional.of(performerPosition), Optional.of(performerPosition));
        setResourceFinderResult(Optional.of(mockResource(besidesPosition)));
        setStructureFinderResult(Optional.empty());

        // Set up for expected action

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionIsMoveToWhenPerformerHasFullInventory() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        // Set up performer
        final Position performerPosition = new Position(5, 5);
        when(performer.getPosition()).thenReturn(performerPosition);
        when(performer.canFitItem(any())).thenReturn(false);
        // Set up other positions
        final Position besidesPosition = performerPosition.subtract(1, 0);
        final Position farAwayPosition = performerPosition.subtract(5, 5);

        //Set up finder results
        setIWorldFinderResult(Optional.of(performerPosition), Optional.empty());
        setResourceFinderResult(Optional.of(mockResource(besidesPosition)));
        StructureFactory.setInventory(new Inventory());
        setStructureFinderResult(Optional.of(StructureFactory.createStructure(StructureType.STOCKPILE,
                                                                              farAwayPosition)));

        // Set up for expected action

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionIdleWhenStructureIsNotAStoreable() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        // Set up performer
        final Position performerPosition = new Position(5, 5);
        when(performer.getPosition()).thenReturn(performerPosition);
        // Set up other positions
        final Position besidesPosition = performerPosition.subtract(1, 0);
        final Position farAwayPosition = performerPosition.subtract(5, 5);

        //Set up finder results
        setIWorldFinderResult(Optional.of(performerPosition), Optional.of(performerPosition));
        setResourceFinderResult(Optional.of(mockResource(besidesPosition)));
        StructureFactory.setInventory(new Inventory());
        setStructureFinderResult(Optional.of(StructureFactory.createStructure(StructureType.HOUSE,
                                                                              farAwayPosition)));

        // Set up for expected action

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionIdleWhenNoEmptyPositionCloseToStockpile() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        // Set up performer
        final Position performerPosition = new Position(5, 5);
        when(performer.getPosition()).thenReturn(performerPosition);
        // Set up other positions
        final Position besidesPosition = performerPosition.subtract(1, 0);
        final Position farAwayPosition = performerPosition.subtract(5, 5);

        //Set up finder results
        setIWorldFinderResult(Optional.of(performerPosition), Optional.of(performerPosition));
        setResourceFinderResult(Optional.of(mockResource(besidesPosition)));
        StructureFactory.setInventory(new Inventory());
        setStructureFinderResult(Optional.of(StructureFactory.createStructure(StructureType.HOUSE,
                                                                              farAwayPosition)),
                                 Optional.empty());

        // Set up for expected action

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionGiveItemWhenPerformerIsNextToStockpile() {
        // Arrange
        final AbstractRole role = createRole();
        final IActionPerformer performer = mock(IActionPerformer.class);
        // Set up performer
        final Position performerPosition = new Position(5, 5);
        when(performer.getPosition()).thenReturn(performerPosition);
        when(performer.canFitItem(any())).thenReturn(false);
        // Set up other positions
        final Position besidesPosition = performerPosition.subtract(1, 0);

        //Set up finder results
        setIWorldFinderResult(Optional.of(performerPosition), Optional.of(performerPosition));
        setResourceFinderResult(Optional.of(mockResource(besidesPosition)));
        StructureFactory.setInventory(new Inventory());
        IStructure structure =
            StructureFactory.createStructure(StructureType.STOCKPILE, besidesPosition);
        setStructureFinderResult(Optional.of(structure));

        // Set up for expected action

        final IAction expected =
            ActionFactory.createGiveItem((IStoreable) structure, besidesPosition);

        // Act
        // To set action to be go to stockpile/empty inven rather than go to resource/harvest
        role.obtainNextAction(performer);
        when(performer.canFitItem(any())).thenReturn(false);
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @SafeVarargs
    private void setStructureFinderResult(
        final Optional<IStructure> firstFinderResult,
        final Optional<IStructure>... OtherFinderResult) {
        when(structureFinder.getNearbyStructureOfType(any(), any())).thenReturn(firstFinderResult,
                                                                                OtherFinderResult);
    }

    @SafeVarargs
    private void setIWorldFinderResult(
        final Optional<Position> firstFinderResult, final Optional<Position>... otherFinderResult) {
        when(mockWorld.getClosestNeighbourOf(any(), any())).thenReturn(firstFinderResult,
                                                                       otherFinderResult);
    }

    @SafeVarargs
    private void setResourceFinderResult(
        final Optional<IResource> firstFinderResult,
        final Optional<IResource>... otherFinderResult) {
        when(finder.getNearbyOfType(any(), any())).thenReturn(firstFinderResult, otherFinderResult);
    }

    private IStructure setUpRoleIsFarAwayFromStockpile(final IActionPerformer performer) {
        final Position stockpilePosition = new Position(0, 0);
        final IStructure stockpile = mock(IStructure.class);
        when(stockpile.getPosition()).thenReturn(stockpilePosition);
        when(stockpile.getType()).thenReturn(StructureType.STOCKPILE);
        when(structureFinder.getNearbyStructureOfType(performer.getPosition(),
                                                      stockpile.getType())).thenReturn(Optional.of(
            stockpile));
        return stockpile;
    }

    private IResource setUpRoleIsNextToTree(final IActionPerformer performer) {
        final Position treePosition = new Position(5, 3);
        final Position besidesPosition = treePosition.subtract(1, 0);
        when(performer.getPosition()).thenReturn(besidesPosition);
        when(performer.canFitItem(any())).thenReturn(true);

        final IResource tree = mockTree(treePosition);
        when(mockWorld.getClosestNeighbourOf(tree, performer.getPosition())).thenReturn(Optional.of(
            besidesPosition));
        return tree;
    }

    private void setUpRoleHasFullInventory(final IActionPerformer performer) {
        when(performer.canFitItem(any())).thenReturn(false);
    }

    /**
     * A harvester role stub-implementation that harvests trees, much like a lumberjack.
     */
    private static final class TestHarvesterRole extends AbstractHarvesterRole {

        TestHarvesterRole(
            final IResourceFinder finder,
            final IStructureFinder structureFinder,
            final IWorld world) {
            super(finder, structureFinder, world, ResourceType.TREE);
        }

        @Override
        public RoleType getType() {
            return null;
        }

    }

}
