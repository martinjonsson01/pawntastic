package com.thebois.models.beings.roles;

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
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureFactory;
import com.thebois.models.world.structures.StructureType;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuilderRoleTests {

    private IWorld world;
    private IStructureFinder structureFinder;
    private AbstractRole role;
    private IActionPerformer performer;
    private IInventory inventory;
    private final ItemType itemType = ItemType.LOG;

    @BeforeEach
    public void setup() {
        world = mock(IWorld.class);
        RoleFactory.setWorld(world);
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        structureFinder = mock(IStructureFinder.class);
        RoleFactory.setStructureFinder(structureFinder);
        ActionFactory.setPathFinder(mock(IPathFinder.class));

        role = RoleFactory.builder();
        performer = mock(IActionPerformer.class);
        when(performer.getPosition()).thenReturn(new Position(0, 0));

        // Set up Idle move to action
        final ITile mockTile = mock(ITile.class);
        final Position randomPosition = new Position(2, 3);
        when(mockTile.getPosition()).thenReturn(randomPosition);
        when(world.getRandomVacantSpotInRadiusOf(any(), anyInt())).thenReturn(mockTile);

        this.inventory = mock(IInventory.class);
        StructureFactory.setInventory(inventory);
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        RoleFactory.setStructureFinder(null);
        ActionFactory.setPathFinder(null);
        StructureFactory.setInventory(null);
    }

    @SafeVarargs
    private void setStructureFinderNearByStructureResult(
        final Optional<IStructure> firstFinderResult,
        final Optional<IStructure>... OtherFinderResult) {
        when(structureFinder.getNearbyStructureOfType(any(), any())).thenReturn(firstFinderResult,
                                                                                OtherFinderResult);
    }

    @SafeVarargs
    private void setStructureFinderIncompleteResult(
        final Optional<IStructure> firstFinderResult,
        final Optional<IStructure>... OtherFinderResult) {
        when(structureFinder.getNearbyIncompleteStructure(any())).thenReturn(firstFinderResult,
                                                                             OtherFinderResult);
    }

    @SafeVarargs
    private void setIWorldFinderResult(
        final Optional<Position> firstFinderResult, final Optional<Position>... otherFinderResult) {
        when(world.getClosestNeighbourOf(any(), any())).thenReturn(firstFinderResult,
                                                                   otherFinderResult);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenNoStockpileWasFound() {
        // Arrange
        setStructureFinderNearByStructureResult(Optional.empty());

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    private IStructure mockStructure(final Position position) {
        final IStructure structure = mock(IStructure.class);
        when(structure.getPosition()).thenReturn(position);
        return structure;
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenNoEmptyPositionNextToStockpileWasFound() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);

        setStructureFinderNearByStructureResult(Optional.of(structure));
        setIWorldFinderResult(Optional.empty());

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsMoveToActionWhenStockpileWasFoundWithEmptyPositionNextToIt() {
        // Arrange
        final Position farAwayPosition = performer.getPosition().subtract(5, 5);
        final IStructure structure = mockStructure(farAwayPosition);

        setStructureFinderNearByStructureResult(Optional.of(structure));
        setIWorldFinderResult(Optional.of(farAwayPosition));

        final IAction expected = ActionFactory.createMoveTo(farAwayPosition);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenStockpileWasNextToPerformerButDisappearLater() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);

        when(performer.isEmpty()).thenReturn(false);
        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.empty());
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenStructureIsNotStorable() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);
        when(performer.isEmpty()).thenReturn(false);
        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsGiveItemActionWhenStructureIsStorable() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure =
            StructureFactory.createStructure(StructureType.STOCKPILE, besidesPerformer);
        final IStoreable storeable = (IStoreable) structure;
        when(performer.isEmpty()).thenReturn(false);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = ActionFactory.createGiveItem(storeable, structure.getPosition());

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenStructureToBuildIsNotFound() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);
        when(performer.isEmpty()).thenReturn(true);

        setStructureFinderNearByStructureResult(Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.empty());
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenStockpileIsGoneWhenFillingInventory() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));
        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(true);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.empty());
        setStructureFinderIncompleteResult(Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenStructureIsNotTakeableWhenFillingInventory() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));
        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(true);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenTakeableDoNotHaveNeededItem() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure =
            StructureFactory.createStructure(StructureType.STOCKPILE, besidesPerformer);
        final ITakeable takeable = (ITakeable) structure;
        when(takeable.hasItem(any())).thenReturn(false);
        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(true);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsTakeItemActionWhenTakeableHaveNeededItem() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure =
            StructureFactory.createStructure(StructureType.STOCKPILE, besidesPerformer);
        final ITakeable takeable = (ITakeable) structure;
        when(takeable.hasItem(any())).thenReturn(true);
        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(true);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final ItemType neededItem = structure.getNeededItems().iterator().next();
        final IAction expected =
            ActionFactory.createTakeItem(takeable, neededItem, structure.getPosition());

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenNoIncompleteStructureWasFound() {
        // Arrange
        final Position farAwayPosition = performer.getPosition().subtract(5, 5);
        final IStructure structure = mockStructure(farAwayPosition);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));

        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(false);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure), Optional.empty());
        setIWorldFinderResult(Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenIncompleteStructureHaveNoEmptyPositionsBesidesIt() {
        // Arrange
        final Position farAwayPosition = performer.getPosition().subtract(5, 5);
        final IStructure structure = mockStructure(farAwayPosition);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));

        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(false);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure), Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()), Optional.empty());

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsMoveToActionWhenIncompleteStructureHasEmptyPositionNextToIt() {
        // Arrange
        final Position farAwayPosition = performer.getPosition().subtract(5, 5);
        final IStructure structure = mockStructure(farAwayPosition);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));

        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(false);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure), Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()), Optional.of(farAwayPosition));

        final IAction expected = ActionFactory.createMoveTo(farAwayPosition);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenIncompleteStructureDisappearedWhenPerformerNextToIt() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));

        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(false);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure),
                                           Optional.of(structure),
                                           Optional.empty());
        setIWorldFinderResult(Optional.of(performer.getPosition()),
                              Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsBuildActionWhenPerformerNextToIncompleteStructureAndHasFullInventory() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));

        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(false);
        when(performer.hasItem(any())).thenReturn(true);

        setStructureFinderNearByStructureResult(Optional.of(structure), Optional.of(structure));
        setStructureFinderIncompleteResult(Optional.of(structure),
                                           Optional.of(structure),
                                           Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()),
                              Optional.of(performer.getPosition()));

        final IAction expected = ActionFactory.createBuild(structure);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void obtainNextActionReturnsIdleActionWhenPerformerHasNoNeededItemsAndNoStockpileWasFound() {
        // Arrange
        final Position besidesPerformer = performer.getPosition().subtract(1, 0);
        final IStructure structure = mockStructure(besidesPerformer);
        when(structure.getNeededItems()).thenReturn(List.of(itemType));

        when(performer.isEmpty()).thenReturn(true);
        when(performer.canFitItem(any())).thenReturn(false);
        when(performer.hasItem(any())).thenReturn(false);

        setStructureFinderNearByStructureResult(Optional.of(structure),
                                                Optional.of(structure),
                                                Optional.empty());
        setStructureFinderIncompleteResult(Optional.of(structure),
                                           Optional.of(structure),
                                           Optional.of(structure));
        setIWorldFinderResult(Optional.of(performer.getPosition()),
                              Optional.of(performer.getPosition()));

        final IAction expected = RoleFactory.idle().obtainNextAction(performer);

        // Act
        final IAction actual = role.obtainNextAction(performer);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

}
