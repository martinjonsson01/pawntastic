package com.thebois.models.beings.tasks;

import java.util.Collection;
import java.util.Stack;

import com.google.common.eventbus.Subscribe;

import com.thebois.ColonyManagement;
import com.thebois.listeners.events.ObstaclePlacedEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.beings.pathfinding.IPathFinder;

/**
 * Moves the performer towards a specified goal.
 */
public class MoveTask implements ITask {

    private final Position destination;
    private final IPathFinder pathFinder;
    private Stack<Position> path = new Stack<>();

    /**
     * Instantiates with a destination to move towards.
     *
     * @param destination The goal to move to.
     * @param pathFinder  A way of generating paths to positions.
     */
    public MoveTask(final Position destination, final IPathFinder pathFinder) {
        this.destination = destination;
        this.pathFinder = pathFinder;
        ColonyManagement.BUS.register(this);
    }

    @Override
    public void perform(final ITaskPerformer performer) {
        performer.setDestination(destination);
    }

    /**
     * Listens to the ObstaclePlacedEvent in order to update pathfinding.
     *
     * @param event The published event.
     */
    @Subscribe
    public void onObstaclePlaced(final ObstaclePlacedEvent event) {
        if (path.contains(event.getPosition())) {
            // Find new path to current goal.
            calculatePathFrom(path.lastElement());
        }
    }

    private void calculatePathFrom(Position start) {
        final Collection<Position> newPath = pathFinder.path(start, destination);
        setPath(newPath);
    }

    private void setPath(final Collection<Position> path) {
        this.path = new Stack<>();
        this.path.addAll(path);
    }

}
