package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing {

    // The current position held by the AbstractBeing.
    private Position currentPosition;
    // The position the AbstractBeing wants to reach.
    private Position destination;

    /**
     * Creates an instance of AbstractBeing.
     */
    public AbstractBeing() {
    }

    /**
     * Creates an AbstractBeing with an initial position.
     *
     * @param currentPosition the initial position of the AbstractBeing.
     * @param destination     the initial destination of the AbstractBeing.
     */
    public AbstractBeing(Position currentPosition, Position destination) {
        this.currentPosition = currentPosition;
        this.destination = destination;
    }

    /**
     * Calculates and sets new position.
     */
    protected void move() {
        final float maxWalkingDistance = 1;

        // Calculate delta of distance between current position and the destination
        final float deltaX = this.destination.getPosX() - this.currentPosition.getPosX();
        final float deltaY = this.destination.getPosY() - this.currentPosition.getPosY();

        // Pythagorean theorem
        final float totalDistance = (float) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        // Calculate walking distance based on distance to destination
        final float updatedWalkingDistance = Math.min(maxWalkingDistance, Math.abs(totalDistance));

        // Calculate norm of distance vector
        final float normDeltaX = deltaX / totalDistance;
        final float normDeltaY = deltaY / totalDistance;

        // Calculate new position
        final float newPosX = this.currentPosition.getPosX() + normDeltaX * updatedWalkingDistance;
        final float newPosY = this.currentPosition.getPosY() + normDeltaY * updatedWalkingDistance;

        // Apply new position to current position
        this.currentPosition.setPosX(newPosX);
        this.currentPosition.setPosY(newPosY);
    }

    @Override
    public Position getPosition() {
        return this.currentPosition;
    }

    @Override
    public void update() {
        move();
    }

}
