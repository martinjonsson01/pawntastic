package com.thebois.models.beings;

import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing, ITaskPerformer {

    // The max speed of the AbstractBeing
    private static final float MAX_WALKING_DISTANCE = 0.1f;
    private Position position;
    private AbstractRole role;
    private Position destination;

    /**
     * Instantiates with an initial position and role.
     *
     * @param startPosition The initial position.
     * @param role          The starting role.
     */
    public AbstractBeing(final Position startPosition, final AbstractRole role) {
        this.position = startPosition;
        this.destination = startPosition;
        this.role = role;
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
        role.obtainNextTask().perform(this);
        move();
    }

    private void move() {
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
    public void setDestination(final Position destination) {
        this.destination = destination;
    }

}
