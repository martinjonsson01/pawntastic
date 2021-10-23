package com.thebois.models.beings.actions;

import java.util.Objects;
import java.util.Optional;

import com.thebois.Pawntastic;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.IStorable;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;

/**
 * Creates actions.
 *
 * @author Mathias
 */
public final class ActionFactory {

    private static IPathFinder pathFinder;

    private ActionFactory() {
    }

    public static void setPathFinder(final IPathFinder pathFinder) {
        ActionFactory.pathFinder = pathFinder;
    }

    /**
     * Creates a movement to a destination.
     *
     * @param destination The location to move to.
     *
     * @return The move action.
     */
    public static IAction createMoveTo(final Position destination) {
        Objects.requireNonNull(pathFinder,
                               "PathFinder needs to be set before calling factory methods.");

        return new MoveAction(destination, pathFinder, Pawntastic::getEventBus);
    }

    /**
     * Creates an action of doing nothing at all.
     *
     * @return A do-nothing action.
     */
    public static IAction createDoNothing() {
        return new DoNothingAction();
    }

    /**
     * Creates a harvesting of a given resource.
     *
     * @param resource What to harvest from.
     *
     * @return The action of harvesting.
     */
    public static IAction createHarvest(final IResource resource) {
        return new HarvestAction(resource);
    }

    /**
     * Creates a construction-task for a given structure.
     *
     * @param toBuild What to construct.
     *
     * @return The construction-task.
     */
    public static IAction createBuild(final IStructure toBuild) {
        return new BuildAction(toBuild);
    }

    /**
     * Creates a give item task.
     *
     * @param storable To where the items should be given.
     * @param position The position of the storable.
     * @param itemType The item to take from performer to storable.
     *
     * @return The give item task.
     */
    public static IAction createGiveItem(
        final IStorable storable, final ItemType itemType, final Position position) {
        return new GiveItemAction(storable, itemType, position);
    }

    /**
     * Creates a take item task.
     *
     * @param takeable From where to items should be taken.
     * @param itemType The type of item to take.
     * @param position The position of the takeable.
     *
     * @return The take item task.
     */
    public static IAction createTakeItem(
        final ITakeable takeable, final ItemType itemType, final Position position) {
        return new TakeItemAction(takeable, itemType, position);
    }

    /**
     * Creates an action of doing skipping an action do next in list.
     *
     * @return A do-next action.
     */
    public static IAction createDoNext() {
        return new DoNextAction();
    }

    /**
     * Creates an action for moving performer to stockpile if possible.
     *
     * @param performer       The performer that should execute the action.
     * @param structureFinder A finder that locates structures.
     * @param world           A finder that locates tiles in the world.
     *
     * @return Either a move to stockpile action or nothing action if stockpile could not be reached
     *     or found.
     */
    public static IAction createMoveToStockpile(
        final IActionPerformer performer,
        final IStructureFinder structureFinder,
        final IWorld world) {
        final Position position = performer.getPosition();
        final Optional<IStructure> maybeStructure =
            structureFinder.getNearbyStructureOfType(position, StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();

        final IStructure structure = maybeStructure.get();

        final Optional<Position> closestSpotNextToStructure =
            world.getClosestNeighbourOf(structure, position);

        if (closestSpotNextToStructure.isEmpty()) {
            return ActionFactory.createDoNothing();
        }
        return ActionFactory.createMoveTo(closestSpotNextToStructure.get());
    }

    /**
     * Creates an action for emptying the performers inventory to a stockpile if possible.
     *
     * @param performer       The performer that needs to empty its inventory.
     * @param structureFinder Finder used to locate a stockpile.
     *
     * @return Either give item action or a nothing action if stockpile could not be reached or
     *     found.
     */
    public static IAction createEmptyInventory(
        final IActionPerformer performer, final IStructureFinder structureFinder) {
        final Optional<IStructure> maybeStructure = structureFinder.getNearbyStructureOfType(
            performer.getPosition(),
            StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure structure = maybeStructure.get();

        if (structure instanceof IStorable) {
            final IStorable storable = (IStorable) structure;
            return ActionFactory.createGiveItem(storable,
                                                performer.takeFirstItem().getType(),
                                                structure.getPosition());
        }
        else {
            return ActionFactory.createDoNothing();
        }
    }

    /**
     * Creates an action for emptying the performers inventory of a certain type of item to a
     * stockpile if possible.
     *
     * @param performer       The performer that needs to empty its inventory.
     * @param structureFinder Finder used to locate a stockpile.
     * @param itemType        The type of item to remove.
     *
     * @return Either give item action or a nothing action if stockpile could not be reached or
     *     found.
     */
    public static IAction createEmptyInventoryOfItemType(
        final IActionPerformer performer,
        final IStructureFinder structureFinder,
        final ItemType itemType) {
        final Optional<IStructure> maybeStructure = structureFinder.getNearbyStructureOfType(
            performer.getPosition(),
            StructureType.STOCKPILE);
        if (maybeStructure.isEmpty()) return ActionFactory.createDoNothing();
        final IStructure structure = maybeStructure.get();
        final IStorable storable;
        if (structure instanceof IStorable) {
            storable = (IStorable) structure;
            return ActionFactory.createGiveItem(storable, itemType, structure.getPosition());
        }
        else {
            return ActionFactory.createDoNothing();
        }
    }

}
