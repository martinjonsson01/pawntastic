package com.thebois.models.beings;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

import com.google.common.eventbus.Subscribe;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.ObstaclePlacedEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.IStructureFinder;

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing {

    // The max speed of the AbstractBeing
    private static final float MAX_WALKING_DISTANCE = 0.1f;
    private final IPathFinder pathFinder;
    private Stack<Position> path;
    private Position position;
    private AbstractRole role;
    private final IStructureFinder finder;

    /**
     * Creates an AbstractBeing with an initial position.
     *
     * @param startPosition The initial position of the AbstractBeing.
     * @param destination   The initial destination of the AbstractBeing.
     * @param pathFinder    The generator of paths to positions in the world.
     * @param finder        Used to find structures in the world.
     */
    public AbstractBeing(
        final Position startPosition,
        final Position destination,
        final IPathFinder pathFinder,
        final IStructureFinder finder) {
        this.position = startPosition;
        this.role = RoleFactory.idle();
        this.pathFinder = pathFinder;
        this.finder = finder;
        setPath(pathFinder.path(startPosition, destination));
        Pawntastic.getEventBus().register(this);
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
            setPath(pathFinder.path(position, path.firstElement()));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition(), getRole());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof AbstractBeing)) return false;
        final AbstractBeing that = (AbstractBeing) other;
        return Objects.equals(getPosition(), that.getPosition()) && Objects.equals(getRole(),
                                                                                   that.getRole());
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

    @Override
    public AbstractRole getRole() {
        return role.deepClone();
    }

    @Override
    public void setRole(final AbstractRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role can not be null. Use IdleRole instead.");
        }
        this.role = role;
    }

    @Override
    public void update() {
        move();
    }

    @Serial
    private void readObject(final java.io.ObjectInputStream in) throws
                                                                IOException,
                                                                ClassNotFoundException {
        // Registers every time on deserialization because it might be registered to an old instance
        // of the event bus.
        // (caused by saving/loading).
        Pawntastic.getEventBus().register(this);
        in.defaultReadObject();
    }

    /**
     * Calculates and sets new position.
     */
    protected void move() {

        if (path.isEmpty()) return;

        final Position destination = path.peek();

        // Calculate delta of distance between current position and the destination
        final float deltaX = destination.getPosX() - this.position.getPosX();
        final float deltaY = destination.getPosY() - this.position.getPosY();

        // Pythagorean theorem
        final float totalDistance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        // Calculate walking distance based on distance to destination
        final float updatedWalkingDistance = Math.min(MAX_WALKING_DISTANCE,
                                                      Math.abs(totalDistance));

        // Calculate norm of distance vector
        final float normDeltaX = deltaX / totalDistance;
        final float normDeltaY = deltaY / totalDistance;

        // To avoid the position being set to NaN
        if (totalDistance == 0) {
            this.position = destination;
            path.pop();
        }
        else {

            // Calculate new position
            final float newPosX = this.position.getPosX() + normDeltaX * updatedWalkingDistance;
            final float newPosY = this.position.getPosY() + normDeltaY * updatedWalkingDistance;

            // Apply new position to current position
            this.position.setPosX(newPosX);
            this.position.setPosY(newPosY);
        }
    }

    @Override
    public Iterable<Position> getPath() {
        final ArrayList<Position> positions = new ArrayList<>(path.size());
        for (final Position pathPosition : path) {
            positions.add(pathPosition.deepClone());
        }
        return positions;
    }

    protected void setPath(final Collection<Position> path) {
        this.path = new Stack<>();
        this.path.addAll(path);
    }

    protected Optional<Position> getDestination() {
        if (path.isEmpty()) return Optional.empty();
        return Optional.of(path.peek().deepClone());
    }

    protected Position getFinalDestination() {
        if (path.isEmpty()) return null;
        return path.firstElement().deepClone();
    }

    protected IPathFinder getPathFinder() {
        return pathFinder;
    }

    protected IStructureFinder getStructureFinder() {
        return this.finder;
    }

    protected Position nearestNeighborOf(final Position destination) {
        final int[][] positionOffsets = {
            {-1, -1}, {0, -1}, {1, -1},
            {-1, 0}, {1, 0},
            {-1, 1}, {0, 1}, {1, 1},
            };

        Position nearestNeighbor = new Position(Float.MAX_VALUE, Float.MAX_VALUE);
        Position lastPosition;

        for (int i = 0; i < positionOffsets.length; i++) {
            final float x = destination.getPosX() + positionOffsets[i][0];
            final float y = destination.getPosY() + positionOffsets[i][1];
            lastPosition = new Position(x, y);

            if (getPosition().distanceTo(nearestNeighbor)
                > getPosition().distanceTo(lastPosition)) {
                nearestNeighbor = lastPosition;
            }
        }
        return nearestNeighbor;
    }

}
