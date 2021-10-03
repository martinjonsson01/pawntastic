package com.thebois.models.beings.actions;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

import com.google.common.eventbus.Subscribe;

import com.thebois.ColonyManagement;
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
    private Deque<Position> path = new LinkedList<>();
    private ITaskPerformer performer;
    private Position latestPosition = new Position(-1, -1);

    /**
     * Instantiates with a destination to move towards.
     *
     * @param destination The goal to move to.
     * @param pathFinder  A way of generating paths to positions.
     */
    MoveAction(final Position destination, final IPathFinder pathFinder) {
        this.destination = destination;
        this.pathFinder = pathFinder;
        ColonyManagement.BUS.register(this);
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
    public void perform(final ITaskPerformer newPerformer) {
        this.performer = newPerformer;
        this.latestPosition = newPerformer.getPosition();
        if (isCompleted()) return;

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
    public boolean isCompleted() {
        return !canReachDestination || latestPosition.equals(destination);
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
            // Find new path to current goal.
            calculatePathFrom(performer.getPosition());
        }
    }

}
