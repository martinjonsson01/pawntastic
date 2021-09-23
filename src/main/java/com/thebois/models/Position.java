package com.thebois.models;

import java.util.Objects;

/**
 * A two-dimensional location.
 */
public final class Position implements IDeepClonable<Position> {

    private float posX;
    private float posY;

    /**
     * Creates position at 0,0.
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Creates position at specified coordinates.
     *
     * @param posX The x-coordinate
     * @param posY The y-coordinate
     */
    public Position(final float posX, final float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Calculates the euclidean distance between two positions.
     *
     * @param other The position to compare distance to
     *
     * @return The distance between the points
     */
    public float distanceTo(final Position other) {
        return distanceTo(other.getPosX(), other.getPosY());
    }

    /**
     * Calculates the euclidean distance between two positions.
     *
     * @param otherPosX The x-coordinate to compare distance to
     * @param otherPosY The y-coordinate to compare distance to
     *
     * @return The distance between the points
     */
    public float distanceTo(final float otherPosX, final float otherPosY) {
        final float distanceX = Math.abs(getPosX() - otherPosX);
        final float distanceY = Math.abs(getPosY() - otherPosY);
        return (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(final float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(final float posY) {
        this.posY = posY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Position position = (Position) o;
        return Float.compare(position.posX, posX) == 0 && Float.compare(position.posY, posY) == 0;
    }

    /**
     * Makes a deep copy of the position and returns it.
     *
     * @return a new Position object with the same coordinates.
     */
    public Position deepClone() {
        return new Position(this.posX, this.posY);
    }

}
