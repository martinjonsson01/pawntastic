package com.thebois.models.beings.roles;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.actions.ActionFactory;
import com.thebois.models.beings.actions.IAction;
import com.thebois.models.beings.actions.IActionSource;
import com.thebois.models.inventory.IStorable;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;

/**
 * The builder constructs buildings.
 */
class BuilderRole extends AbstractRole {

    private static final int NUMBER_OF_BUILDING_STATES = 2;
    /**
     * Depending on what state the role is in, the performer gets different actions. Either go to
     * stockpile and fill/empty inventory or go to incomplete building and give it items.
     */
    private BuilderState builderState = BuilderState.FETCHING_RESOURCES;

    /**
     * Whether the performer's inventory should be filled or emptied.
     */
    private enum BuilderState {
        FETCHING_RESOURCES, BUILDING_STRUCTURES
    }

    private boolean isFilling = false;
    private final IStructureFinder structureFinder;
    private final IWorld world;

    /**
     * Instantiates with a way of finding structures to build.
     *
     * @param structureFinder The source of structures to construct.
     * @param world           The world in which the structures are located.
     */
    BuilderRole(
        final IStructureFinder structureFinder, final IWorld world) {
        this.structureFinder = structureFinder;
        this.world = world;
    }

    @Override
    public RoleType getType() {
        return RoleType.BUILDER;
    }

    @Override
    protected Collection<IActionSource> getTaskGenerators() {

        if (builderState == BuilderState.FETCHING_RESOURCES) {
            return List.of(this::createMoveToStockpile,
                           this::createEmptyInventory,
                           this::createFillInventory);
        }
        else {
            return List.of(this::createMoveToIncompleteStructure, this::createBuildStructure);
        }
    }

    private IAction createMoveToStockpile(final IActionPerformer performer) {
        return ActionFactory.createMoveToStockpile(performer, structureFinder, world);
    }

    private IAction createEmptyInventory(final IActionPerformer performer) {
        if (isFilling) {
            return ActionFactory.createDoNext();
        }
        if (performer.isEmpty()) {
            isFilling = true;
            return ActionFactory.createDoNext();
        }
        final Optional<IStructure> maybeStructure = structureFinder.getNearbyStructureOfType(
            performer.getPosition(),
            StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure structure = maybeStructure.get();

        if (structure instanceof IStorable) {
            final IStorable storable = (IStorable) structure;
            return ActionFactory.createGiveItem(storable, structure.getPosition());
        }
        else {
            return ActionFactory.createDoNothing();
        }
    }

    private IAction createFillInventory(final IActionPerformer performer) {
        final Optional<IStructure> maybeStructure = findNearbyIncompleteStructure(performer);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure structure = maybeStructure.get();

        final ItemType nextNeededItem = getNextNeededItem(performer, structure);
        // Can performer fit item
        if (nextNeededItem == null || !performer.canFitItem(nextNeededItem)) {
            setBuildingState();
            return ActionFactory.createDoNext();
        }

        // Check if stockpile still exists and has item
        final Optional<IStructure> maybeStockpile = structureFinder.getNearbyStructureOfType(
            performer.getPosition(),
            StructureType.STOCKPILE);
        if (maybeStockpile.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure stockpile = maybeStockpile.get();

        if (stockpile instanceof ITakeable) {
            final ITakeable takeable = (ITakeable) stockpile;
            if (!takeable.hasItem(nextNeededItem)) {
                setBuildingState();
                return ActionFactory.createDoNext();
            }
            return ActionFactory.createTakeItem(takeable, nextNeededItem, stockpile.getPosition());
        }
        else {
            return ActionFactory.createDoNothing();
        }
    }

    private void setBuildingState() {
        builderState = BuilderState.BUILDING_STRUCTURES;
        isFilling = false;
    }

    private ItemType getNextNeededItem(
        final IActionPerformer performer, final IStructure structure) {
        ItemType nextNeededItem = null;
        for (final ItemType type : ItemType.values()) {
            if (!performer.hasItem(type, structure.getNumberOfNeededItem(type))) {
                nextNeededItem = type;
            }
        }
        return nextNeededItem;
    }

    private IAction createMoveToIncompleteStructure(final IActionPerformer performer) {
        final Optional<IStructure> maybeStructure = findNearbyIncompleteStructure(performer);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();

        final Optional<Position> closestSpotNextToStructure =
            world.getClosestNeighbourOf(structure, performer.getPosition());

        if (closestSpotNextToStructure.isEmpty()) return ActionFactory.createDoNothing();

        return ActionFactory.createMoveTo(closestSpotNextToStructure.get());
    }

    private IAction createBuildStructure(final IActionPerformer performer) {

        final Optional<IStructure> maybeStructure = findNearbyIncompleteStructure(performer);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();
        final Iterable<ItemType> neededItems = structure.getNeededItems();

        boolean performerHasNeededItem = false;
        for (final ItemType neededItem : neededItems) {
            if (performer.hasItem(neededItem)) {
                performerHasNeededItem = true;
            }
        }

        if (!performerHasNeededItem) {
            builderState = BuilderState.FETCHING_RESOURCES;
            return ActionFactory.createDoNext();
        }

        return ActionFactory.createBuild(structure);
    }

    private Optional<IStructure> findNearbyIncompleteStructure(
        final IActionPerformer performer) {
        final Position position = performer.getPosition();
        return structureFinder.getNearbyIncompleteStructure(position);
    }

}
