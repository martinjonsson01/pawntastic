package com.thebois.models.beings;

import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.RoleFactory;

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing, ITaskPerformer {

    // The max speed of the AbstractBeing
    private static final float MAX_WALKING_DISTANCE = 0.1f;
    // The position the AbstractBeing wants to reach.
    private Position destination;
    // The current position held by the AbstractBeing.
    private Position currentPosition;
    private AbstractRole role;

    /**
     * Creates an AbstractBeing with an initial position.
     *
     * @param currentPosition the initial position of the AbstractBeing.
     * @param destination     the initial destination of the AbstractBeing.
     */
    public AbstractBeing(final Position currentPosition, final Position destination) {
        this.currentPosition = currentPosition;
        this.destination = destination;
        this.role = RoleFactory.idle();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDestination(), getPosition(), getRole());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBeing)) return false;
        final AbstractBeing that = (AbstractBeing) o;
        return Objects.equals(getDestination(), that.getDestination())
               && Objects.equals(currentPosition,
                                 that.currentPosition)
               && Objects.equals(getRole(), that.getRole());
    }

    protected Position getDestination() {
        return destination.deepClone();
    }

    @Override
    public Position getPosition() {
        return currentPosition.deepClone();
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

        // Calculate delta of distance between current position and the destination
        final float deltaX = this.destination.getPosX() - this.currentPosition.getPosX();
        final float deltaY = this.destination.getPosY() - this.currentPosition.getPosY();

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
            this.currentPosition = destination;
        }
        else {

            // Calculate new position
            final float newPosX =
                this.currentPosition.getPosX() + normDeltaX * updatedWalkingDistance;
            final float newPosY =
                this.currentPosition.getPosY() + normDeltaY * updatedWalkingDistance;

            // Apply new position to current position
            this.currentPosition.setPosX(newPosX);
            this.currentPosition.setPosY(newPosY);
        }
    }

    protected void setDestination(final Position destination) {
        this.destination = destination;
    }

}
