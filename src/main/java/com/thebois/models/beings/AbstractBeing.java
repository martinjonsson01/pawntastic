package com.thebois.models.beings;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing {

    // The max speed of the AbstractBeing
    private static final float MAX_WALKING_DISTANCE = 0.1f;
    private final IPathFinder pathFinder;
    private Queue<Position> path;
    private Position position;
    private AbstractRole role;

    /**
     * Creates an AbstractBeing with an initial position.
     *
     * @param startPosition the initial position of the AbstractBeing.
     * @param destination   the initial destination of the AbstractBeing.
     * @param pathFinder    The generator of paths to positions in the world.
     */
    public AbstractBeing(final Position startPosition,
                         final Position destination,
                         final IPathFinder pathFinder) {
        this.position = startPosition;
        this.role = RoleFactory.idle();
        this.pathFinder = pathFinder;
        this.path = new ArrayDeque<>(pathFinder.path(startPosition, destination));
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
    public void update() {
        move();
    }

    /**
     * Calculates and sets new position.
     */
    protected void move() {

        final Position destination = path.peek();

        if (destination == null) return;

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
            path.remove();
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
        this.path = new ArrayDeque<>(path);
    }

    protected Optional<Position> getDestination() {
        final Position destination = path.peek();
        if (destination == null) return Optional.empty();
        return Optional.of(destination.deepClone());
    }

    protected IPathFinder getPathFinder() {
        return pathFinder;
    }

}
