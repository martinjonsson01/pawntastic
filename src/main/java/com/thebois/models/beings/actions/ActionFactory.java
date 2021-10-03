package com.thebois.models.beings.actions;

import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;

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
     * Creates a task that represents moving to a destination.
     *
     * @param destination The location to move to.
     *
     * @return The move task.
     */
    public static IAction createMoveTo(final Position destination) {
        Objects.requireNonNull(pathFinder,
                               "PathFinder needs to be set before calling factory methods.");
        return new MoveAction(destination, pathFinder);
    }

}
