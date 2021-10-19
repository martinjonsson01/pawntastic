package com.thebois.models;

import java.io.Serializable;
import java.util.Objects;

import com.thebois.abstractions.IDeepClonable;

/**
 * A two-dimensional location.
 */
public final class Position implements IDeepClonable<Position>, Serializable {

    private float x;
    private float y;

    /**
     * Creates position at 0,0.
     */
    public Position() {
        this(0, 0);
    }

    /**
     * Creates position at specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Position(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the euclidean distance between two positions.
     *
     * @param other The position to compare distance to.
     *
     * @return The distance between the points.
     */
    public float distanceTo(final Position other) {
        return distanceTo(other.getX(), other.getY());
    }

    /**
     * Calculates the euclidean distance between two positions.
     *
     * @param otherX The x-coordinate to compare distance to.
     * @param otherY The y-coordinate to compare distance to.
     *
     * @return The distance between the points.
     */
    public float distanceTo(final float otherX, final float otherY) {
        final float distanceX = Math.abs(getX() - otherX);
        final float distanceY = Math.abs(getY() - otherY);
        return (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    }

    public float getX() {
        return x;
    }

    public void setX(final float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(final float y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
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
        return Float.compare(position.x, x) == 0 && Float.compare(position.y, y) == 0;
    }

    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }

    /**
     * Makes a deep copy of the position and returns it.
     *
     * @return a new Position object with the same coordinates.
     */
    @Override
    public Position deepClone() {
        return new Position(this.x, this.y);
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
        final float distanceX = Math.abs(getX() - destination.getX());
        final float distanceY = Math.abs(getY() - destination.getY());
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
        return add(other.getX(), other.getY());
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
        return new Position(x + otherX, y + otherY);
    }

    /**
     * Subtracts a position component-wise.
     *
     * @param other The position to subtract.
     *
     * @return The difference.
     */
    public Position subtract(final Position other) {
        return subtract(other.getX(), other.getY());
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
        return new Position(x * scalar, y * scalar);
    }

}
