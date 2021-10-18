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

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing {

    // The max speed of the being, in tiles/second.
    private static final float SPEED = 6f;
    private static final float DESTINATION_REACHED_DISTANCE = 0.01f;
    private final IPathFinder pathFinder;
    private Position position;
    private Stack<Position> path;
    private AbstractRole role;

    /**
     * Creates an AbstractBeing with an initial position.
     *
     * @param startPosition the initial position of the AbstractBeing.
     * @param destination   the initial destination of the AbstractBeing.
     * @param pathFinder    The generator of paths to positions in the world.
     */
    public AbstractBeing(
        final Position startPosition, final Position destination, final IPathFinder pathFinder) {
        this.position = startPosition;
        this.role = RoleFactory.idle();
        this.pathFinder = pathFinder;
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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBeing)) return false;
        final AbstractBeing that = (AbstractBeing) o;
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
    public void update(final float deltaTime) {
        move(deltaTime);
    }

    /**
     * Calculates and sets new position.
     *
     * @param deltaTime How much time the being should move at its speed forward, in seconds.
     */
    protected void move(final float deltaTime) {
        if (path.isEmpty()) return;

        final Position segmentDestination = path.peek();

        final float distanceToDestination = segmentDestination.distanceTo(getPosition());

        if (distanceToDestination < DESTINATION_REACHED_DISTANCE) {
            onArrivedAtDestination(segmentDestination);
            return;
        }

        movePositionTowardsDestination(deltaTime, segmentDestination, distanceToDestination);
    }

    private void onArrivedAtDestination(final Position segmentDestination) {
        position = segmentDestination;
        path.pop();
    }

    private void movePositionTowardsDestination(
        final float deltaTime, final Position segmentDestination, final float totalDistance) {
        // Calculate how much to move and in what direction.
        final Position delta = segmentDestination.subtract(position);
        final Position direction = delta.multiply(1f / totalDistance);
        final Position velocity = direction.multiply(SPEED);
        final Position movement = velocity.multiply(deltaTime);

        Position newPosition = position.add(movement);

        if (hasOvershotDestination(segmentDestination, delta, newPosition)) {
            // Clamp position to destination,
            // to prevent walking past the destination during large time skips.
            newPosition = segmentDestination;
        }

        position = newPosition;
    }

    private boolean hasOvershotDestination(
        final Position destination, final Position delta, final Position newPosition) {
        // Destination has been overshot if the delta has changed sign before and after moving.
        final Position newDelta = destination.subtract(newPosition);
        return hasChangedSign(newDelta.getPosX(), delta.getPosX())
               || hasChangedSign(newDelta.getPosY(), delta.getPosY());
    }

    private boolean hasChangedSign(final float posX, final float posX2) {
        return Math.signum(posX) != Math.signum(posX2);
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

    protected Optional<Position> getDestination() {
        if (path.isEmpty()) return Optional.empty();
        return Optional.of(path.peek().deepClone());
    }

    protected IPathFinder getPathFinder() {
        return pathFinder;
    }

}
