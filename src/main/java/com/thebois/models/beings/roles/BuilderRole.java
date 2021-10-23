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

    private static final int NUMBER_OF_BUILDING_STATES = 3;
    private final IStructureFinder finder;
    private final IWorld world;
    private int buildingState = 0;

    /**
     * Instantiates with a way of finding structures to build.
     *
     * @param finder The source of structures to construct.
     * @param world  The world in which the structures are located.
     */
    BuilderRole(
        final IStructureFinder finder, final IWorld world) {
        this.finder = finder;
        this.world = world;
    }

    @Override
    public RoleType getType() {
        return RoleType.BUILDER;
    }

    @Override
    protected Collection<IActionSource> getTaskGenerators() {
        buildingState %= NUMBER_OF_BUILDING_STATES;
        switch (buildingState) {
            case 0 -> {
                return List.of(this::createMoveToStockpile, this::createEmptyInventory);
            }
            case 1 -> {
                return List.of(this::createFillInventory);
            }
            default -> {
                return List.of(this::createMoveToIncompleteStructure, this::createBuildStructure);
            }
        }
    }

    private IAction createMoveToStockpile(final IActionPerformer performer) {
        final Position position = performer.getPosition();
        final Optional<IStructure> maybeStructure =
            finder.getNearbyStructureOfType(position, StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();

        final Optional<Position> closestSpotNextToStructure =
            world.getClosestNeighbourOf(structure, position);

        if (closestSpotNextToStructure.isEmpty()) return ActionFactory.createDoNothing();

        return ActionFactory.createMoveTo(closestSpotNextToStructure.get());
    }

    private IAction createEmptyInventory(final IActionPerformer performer) {
        if (performer.isEmpty()) {
            buildingState++;
            return ActionFactory.createDoNext();
        }
        final Position position = performer.getPosition();
        final Optional<IStructure> maybeStructure =
            finder.getNearbyStructureOfType(position, StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure structure = maybeStructure.get();
        final IStorable storable;
        if (structure instanceof IStorable) {
            storable = (IStorable) structure;
        }
        else {
            return ActionFactory.createDoNothing();
        }

        return ActionFactory.createGiveItem(storable, structure.getPosition());
    }

    private IAction createFillInventory(final IActionPerformer performer) {
        final Optional<IStructure> maybeStructure = findNearbyIncompleteStructure(performer);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure structure = maybeStructure.get();

        final Collection<ItemType> neededItems = structure.getNeededItems();
        final ItemType nextNeedItem = neededItems.iterator().next();

        // Can performer fit item
        if (!performer.canFitItem(nextNeedItem)) {
            buildingState++;
            return ActionFactory.createDoNext();
        }

        // Check if stockpile still exists and has item
        final Position position = performer.getPosition();
        final Optional<IStructure> maybeStockpile =
            finder.getNearbyStructureOfType(position, StructureType.STOCKPILE);
        if (maybeStockpile.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure stockpile = maybeStockpile.get();

        final ITakeable takeable;
        if (stockpile instanceof ITakeable) {
            takeable = (ITakeable) stockpile;
        }
        else {
            return ActionFactory.createDoNothing();
        }
        if (!takeable.hasItem(nextNeedItem)) {
            return ActionFactory.createDoNothing();
        }

        return ActionFactory.createTakeItem(takeable, nextNeedItem, stockpile.getPosition());
    }

    private IAction createMoveToIncompleteStructure(final IActionPerformer performer) {
        final Optional<IStructure> maybeStructure = findNearbyIncompleteStructure(performer);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();

        final Position position = performer.getPosition();
        final Optional<Position> closestSpotNextToStructure =
            world.getClosestNeighbourOf(structure, position);

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
            buildingState++;
            return ActionFactory.createDoNext();
        }

        return ActionFactory.createBuild(structure);
    }

    private Optional<IStructure> findNearbyIncompleteStructure(
        final IActionPerformer performer) {
        final Position position = performer.getPosition();
        return finder.getNearbyIncompleteStructure(position);
    }

}
