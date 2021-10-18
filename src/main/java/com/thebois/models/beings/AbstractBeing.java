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
    private static final float MOVEMENT_SPEED = 6f;
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

        final Position destination = path.peek();

        // Calculate delta of distance between current position and the destination
        final float deltaX = destination.getPosX() - this.position.getPosX();
        final float deltaY = destination.getPosY() - this.position.getPosY();

        final float totalDistance = destination.distanceTo(getPosition());

        if (totalDistance < DESTINATION_REACHED_DISTANCE) {
            position = destination;
            path.pop();
            return;
        }

        final float directionX = deltaX / totalDistance;
        final float directionY = deltaY / totalDistance;

        final float velocityX = directionX * MOVEMENT_SPEED;
        final float velocityY = directionY * MOVEMENT_SPEED;

        final float oldX = getPosition().getPosX();
        final float oldY = getPosition().getPosY();
        float newX = oldX + velocityX * deltaTime;
        float newY = oldY + velocityY * deltaTime;

        // Clamp position to destination,
        // to prevent walking past the destination during large time skips.
        final float newDeltaX = destination.getPosX() - newX;
        final float newDeltaY = destination.getPosY() - newY;
        if (Math.signum(newDeltaX) != Math.signum(deltaX)) newX = destination.getPosX();
        if (Math.signum(newDeltaY) != Math.signum(deltaY)) newY = destination.getPosY();

        position.setPosX(newX);
        position.setPosY(newY);
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
