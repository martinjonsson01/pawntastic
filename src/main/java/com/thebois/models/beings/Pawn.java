package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * Pawn.
 */
public class Pawn implements IBeing {

    private final Position currentPosition;
    private Position destination;

    /**
     * Creates a Pawn.
     *
     * @param currentPosition the initial position of the pawn.
     */
    public Pawn(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    // proof of concept, preliminary function
    private void move() {
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

    @Override
    public void walkTo(Position position) {
        this.destination = position;
    }

    @Override
    public void update() {
        move();
    }

}
