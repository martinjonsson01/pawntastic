package com.thebois.models.beings;

import com.thebois.models.Position;

import java.lang.Math;

public class Pawn implements IBeing {

    private final Position currentPosition;
    private Position destination;

    public Pawn(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    // proof of concept, preliminary function
    private void move() {
        float maxWalkingDistance = 1;

        // Calculate delta of distance between current position and the destination
        float deltaX = this.destination.getPosX() - this.currentPosition.getPosX();
        float deltaY = this.destination.getPosY() - this.currentPosition.getPosY();

        // Decide whether to walk the maximum distance or to walk a shorter distance
        float walkDistanceY = (Math.min(Math.abs(maxWalkingDistance), Math.abs(deltaY)));
        float walkDistanceX = (Math.min(Math.abs(maxWalkingDistance), Math.abs(deltaX)));

        // Calculate direction of the distance to walk
        float distanceX = Math.signum(deltaX) * walkDistanceX;
        float distanceY = Math.signum(deltaY) * walkDistanceY;

        // Calculate new position
        float newXPos = this.currentPosition.getPosX() + distanceX;
        float newYPos = this.currentPosition.getPosY() + distanceY;

        // Apply new position to current position
        this.currentPosition.setPosX(newXPos);
        this.currentPosition.setPosY(newYPos);
    }

    @Override
    public Position getPosition() {
        return this.currentPosition;
    }

    @Override
    public void walkTo(Position destination) {
        this.destination = destination;
    }

    @Override
    public void update() {
        move();
    }

}
