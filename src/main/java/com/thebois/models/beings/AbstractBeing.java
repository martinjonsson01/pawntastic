package com.thebois.models.beings;

import java.util.Random;

import com.thebois.models.IDeepClonable;
import com.thebois.models.Position;

/**
 * An abstract implementation of IBeing.
 */
public abstract class AbstractBeing implements IBeing, IDeepClonable<IBeing> {

    // The current position held by the AbstractBeing.
    private Position currentPosition;
    // The position the AbstractBeing wants to reach.
    private Position destination;
    // The max speed of the AbstractBeing
    private final float maxWalkingDistance = 5;
    // temporary: n*n world size
    private final int worldSize = 500;
    private Random random = new Random();

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
    public AbstractBeing(Position currentPosition, Position destination) {
        this.currentPosition = currentPosition;
        this.destination = destination;
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
        final float updatedWalkingDistance = Math.min(maxWalkingDistance, Math.abs(totalDistance));

        // Calculate norm of distance vector
        final float normDeltaX = deltaX / totalDistance;
        final float normDeltaY = deltaY / totalDistance;

        // To avoid the position being set to NaN
        if (Float.isNaN(normDeltaX) || Float.isNaN(normDeltaY)) {
            this.currentPosition = destination;
        }
        else {

            // Calculate new position
            final float
                newPosX =
                this.currentPosition.getPosX() + normDeltaX * updatedWalkingDistance;
            final float
                newPosY =
                this.currentPosition.getPosY() + normDeltaY * updatedWalkingDistance;

            // Apply new position to current position
            this.currentPosition.setPosX(newPosX);
            this.currentPosition.setPosY(newPosY);
        }
    }

    protected Position getDestination() {
        return this.destination;
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
