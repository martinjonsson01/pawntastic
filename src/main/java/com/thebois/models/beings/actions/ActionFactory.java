package com.thebois.models.beings.actions;

import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.structures.IStructure;

/**
 * Creates actions.
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
        return new MoveAction(destination, pathFinder);
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
     * @param storeable To where the items should be given.
     * @param itemType  What type of item to receive.
     * @param position  The position of the storable.
     *
     * @return The give item task.
     */
    public static IAction createGiveItem(
        final IStoreable storeable,
        final ItemType itemType,
        final Position position) {
        return new GiveItemAction(storeable, itemType, position);
    }

    /**
     * Creates a take item task.
     *
     * @param takeable From where to items should be taken.
     * @param itemType The type of item to take.
     * @param amount   The amount of items to take.
     * @param position The position of the takeable.
     *
     * @return The take item task.
     */
    public static IAction createTakeItem(
        final ITakeable takeable,
        final ItemType itemType,
        final int amount,
        final Position position) {
        return new TakeItemAction(takeable, itemType, amount, position);
    }

}
