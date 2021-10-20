package com.thebois.models.beings.actions;

import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
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

}
