package com.thebois.models.beings.tasks;

import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;

/**
 * Creates tasks.
 */
public final class TaskFactory {

    private static IPathFinder pathFinder;

    private TaskFactory() {
    }

    public static void setPathFinder(final IPathFinder pathFinder) {
        TaskFactory.pathFinder = pathFinder;
    }

    /**
     * Creates a task that represents moving to a destination.
     *
     * @param destination The location to move to.
     *
     * @return The move task.
     */
    public static ITask createMoveTo(final Position destination) {
        Objects.requireNonNull(pathFinder,
                               "PathFinder needs to be set before calling factory methods.");
        return new MoveTask(destination, pathFinder);
    }

}
