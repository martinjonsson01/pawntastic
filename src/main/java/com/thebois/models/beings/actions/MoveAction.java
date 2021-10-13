package com.thebois.models.beings.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import com.google.common.eventbus.Subscribe;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.ObstaclePlacedEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.pathfinding.IPathFinder;

/**
 * Moves the performer towards a specified goal.
 */
class MoveAction implements IAction {

    private final Position destination;
    private final IPathFinder pathFinder;
    private boolean canReachDestination = true;
    private LinkedList<Position> path = new LinkedList<>();

    /**
     * Instantiates with a destination to move towards.
     *
     * @param destination The goal to move to.
     * @param pathFinder  A way of generating paths to positions.
     */
    MoveAction(final Position destination, final IPathFinder pathFinder) {
        this.destination = destination;
        this.pathFinder = pathFinder;
        Pawntastic.BUS.register(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveAction)) return false;
        final MoveAction moveTask = (MoveAction) o;
        return destination.equals(moveTask.destination);
    }

    @Override
    public void perform(final ITaskPerformer performer) {
        if (isCompleted(performer)) return;

        final Position position = performer.getPosition();
        if (path.isEmpty()) {
            calculatePathFrom(position);
            if (path.isEmpty()) {
                canReachDestination = false;
                return;
            }
        }

        if (position.equals(path.element())) {
            path.remove();
        }

        performer.setDestination(path.element());
    }

    @Override
    public boolean isCompleted(final ITaskPerformer performer) {
        return !canReachDestination || performer.getPosition().equals(destination);
    }

    private void calculatePathFrom(final Position start) {
        final Collection<Position> newPath = pathFinder.path(start, destination);
        setPath(newPath);
    }

    private void setPath(final Collection<Position> path) {
        this.path = new LinkedList<>();
        this.path.addAll(path);
    }

    /**
     * Listens to the ObstaclePlacedEvent in order to update pathfinding.
     *
     * @param event The published event.
     */
    @Subscribe
    public void onObstaclePlaced(final ObstaclePlacedEvent event) {
        if (path.isEmpty()) return;

        if (path.contains(event.getPosition())) {
            recalculatePathAroundObstacle(event.getPosition());
        }
    }

    private void recalculatePathAroundObstacle(final Position obstaclePosition) {
        final int obstacleInPathIndex = path.indexOf(obstaclePosition);
        final int nodeBeforeObstacleIndex = Math.max(0, obstacleInPathIndex - 1);
        final Position pathNodeBeforeObstacle = path.get(nodeBeforeObstacleIndex);

        final Collection<Position> recalculatedPathSegment = pathFinder.path(pathNodeBeforeObstacle,
                                                                             destination);

        // If not possible to find another path to the destination.
        if (recalculatedPathSegment.isEmpty()) {
            canReachDestination = false;
            return;
        }

        final int originalSegmentEndIndex = Math.max(0, nodeBeforeObstacleIndex - 1);
        final Collection<Position> originalPathSegment = path.subList(0, originalSegmentEndIndex);

        final Collection<Position> recalculatedPath = new ArrayList<>(originalPathSegment);
        recalculatedPath.addAll(recalculatedPathSegment);
        setPath(recalculatedPath);
    }

}
