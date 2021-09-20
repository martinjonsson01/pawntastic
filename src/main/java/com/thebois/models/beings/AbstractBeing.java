package com.thebois.models.beings;

import java.util.Random;

import com.thebois.models.Position;

public class AbstractBeing implements IBeing {

    // The current position held by the AbstractBeing.
    protected Position currentPosition;
    // The position the AbstractBeing wants to reach.
    protected Position destination;

    /**
     * Creates an AbstractBeing.
     */
    public AbstractBeing() {
    }

    /**
     * Creates an AbstractBeing and generates a random initial position and destination.
     *
     * @param random random number generator.
     */
    public AbstractBeing(Random random) {
        this.currentPosition = new Position((float) random.nextInt(), (float) random.nextInt());
        this.destination = new Position((float) random.nextInt(), (float) random.nextInt());
    }

    /**
     * Creates an AbstractBeing with an initial position.
     *
     * @param currentPosition the initial position of the AbstractBeing.
     * @param destination the initial destination of the AbstractBeing.
     */
    public AbstractBeing(Position currentPosition, Position destination) {
        this.currentPosition = currentPosition;
        this.destination = destination;
    }

    // proof of concept, preliminary function
    protected void move() {
        final float maxWalkingDistance = 1;

        // Calculate delta of distance between current position and the destination
        final float deltaX = this.destination.getPosX() - this.currentPosition.getPosX();
        final float deltaY = this.destination.getPosY() - this.currentPosition.getPosY();

        // Decide whether to walk the maximum distance or to walk a shorter distance
        final float walkDistanceY = Math.min(Math.abs(maxWalkingDistance), Math.abs(deltaY));
        final float walkDistanceX = Math.min(Math.abs(maxWalkingDistance), Math.abs(deltaX));

        // Calculate direction of the distance to walk
        final float distanceX = Math.signum(deltaX) * walkDistanceX;
        final float distanceY = Math.signum(deltaY) * walkDistanceY;

        // Calculate new position
        final float newPosX = this.currentPosition.getPosX() + distanceX;
        final float newPosY = this.currentPosition.getPosY() + distanceY;

        // Apply new position to current position
        this.currentPosition.setPosX(newPosX);
        this.currentPosition.setPosY(newPosY);
    }

    @Override
    public Position getPosition() {
        return this.currentPosition;
    }

    protected void walkTo(Position position) {
        this.destination = position;
    }

    @Override
    public void update() {
        move();
    }

}
