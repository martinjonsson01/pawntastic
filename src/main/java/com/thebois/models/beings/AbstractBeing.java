package com.thebois.models.beings;

import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing {

    // The max speed of the AbstractBeing
    private static final float MAX_WALKING_DISTANCE = 0.1f;
    // The position the AbstractBeing wants to reach.
    private Position destination;
    // The current position held by the AbstractBeing.
    private Position currentPosition;
    private AbstractRole role;

    /**
     * Creates an instance of AbstractBeing.
     */
    public AbstractBeing() {
        this.destination = new Position();
        this.currentPosition = new Position();
    }

    /**
     * Creates an AbstractBeing with an initial position.
     *
     * @param currentPosition the initial position of the AbstractBeing.
     * @param destination     the initial destination of the AbstractBeing.
     */
    public AbstractBeing(final Position currentPosition, final Position destination) {
        this.currentPosition = currentPosition;
        this.destination = destination;
    }

    @Override
    public Position getPosition() {
        return this.currentPosition;
    }

    @Override
    public AbstractRole getRole() {
        if (role == null) return null;
        return role.deepClone();
    }

    @Override
    public void setRole(final AbstractRole role) {
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
        if (Float.isNaN(normDeltaX) || Float.isNaN(normDeltaY)) {
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

    @Override
    public int hashCode() {
        return Objects.hash(getDestination(), currentPosition, getRole());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBeing)) return false;
        final AbstractBeing that = (AbstractBeing) o;
        return Objects.equals(getDestination(), that.getDestination()) && Objects.equals(
            currentPosition,
            that.currentPosition) && Objects.equals(getRole(), that.getRole());
    }

    protected Position getDestination() {
        return this.destination;
    }

    protected void setDestination(final Position destination) {
        this.destination = destination;
    }

}
