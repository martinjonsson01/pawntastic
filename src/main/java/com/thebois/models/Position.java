package com.thebois.models;

import java.io.Serializable;
import java.util.Objects;

import com.thebois.abstractions.IDeepClonable;

/**
 * A two-dimensional location.
 */
public final class Position implements IDeepClonable<Position>, Serializable {

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
     * @param posX The x-coordinate.
     * @param posY The y-coordinate.
     */
    public Position(final float posX, final float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Calculates the euclidean distance between two positions.
     *
     * @param other The position to compare distance to.
     *
     * @return The distance between the points.
     */
    public float distanceTo(final Position other) {
        return distanceTo(other.getPosX(), other.getPosY());
    }

    /**
     * Calculates the euclidean distance between two positions.
     *
     * @param otherPosX The x-coordinate to compare distance to.
     * @param otherPosY The y-coordinate to compare distance to.
     *
     * @return The distance between the points.
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
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Position position = (Position) other;
        return Float.compare(position.posX, posX) == 0 && Float.compare(position.posY, posY) == 0;
    }

    @Override
    public String toString() {
        return "{" + "x=" + posX + ", y=" + posY + '}';
    }

    /**
     * Makes a deep copy of the position and returns it.
     *
     * @return a new Position object with the same coordinates.
     */
    @Override
    public Position deepClone() {
        return new Position(this.posX, this.posY);
    }

    /**
     * Calculates the Manhattan distance to the destination.
     *
     * <p>
     * Manhattan distance is distance calculated through a grid. Doesn't allow diagonal movements.
     * </p>
     *
     * @param destination The position to check the distance to.
     *
     * @return The manhattan distance to the destination.
     */
    public int manhattanDistanceTo(final Position destination) {
        final float distanceX = Math.abs(getPosX() - destination.getPosX());
        final float distanceY = Math.abs(getPosY() - destination.getPosY());
        return Math.round(distanceX + distanceY);
    }

    /**
     * Adds a position component-wise.
     *
     * @param other The position to add.
     *
     * @return The sum of the positions.
     */
    public Position add(final Position other) {
        return add(other.getPosX(), other.getPosY());
    }

    /**
     * Adds the given values to each component.
     *
     * @param otherX How much to add to the x-coordinate.
     * @param otherY How much to add to the y-coordinate.
     *
     * @return A position with its coordinates added to.
     */
    public Position add(final float otherX, final float otherY) {
        return new Position(posX + otherX, posY + otherY);
    }

    /**
     * Subtracts a position component-wise.
     *
     * @param other The position to subtract.
     *
     * @return The difference.
     */
    public Position subtract(final Position other) {
        return subtract(other.getPosX(), other.getPosY());
    }

    /**
     * Subtracts components by the given values.
     *
     * @param otherX How much to subtract from the x-coordinate.
     * @param otherY How much to subtract from the y-coordinate.
     *
     * @return A position with its coordinates subtracted from.
     */
    public Position subtract(final float otherX, final float otherY) {
        return add(-otherX, -otherY);
    }

    /**
     * Scales the position by a given value.
     *
     * @param scalar The factor to scale by.
     *
     * @return The scaled position.
     */
    public Position multiply(final float scalar) {
        return new Position(posX * scalar, posY * scalar);
    }

}
