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

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY);
    }

    @Override
    public Position deepClone() {
        return new Position(this.posX, this.posY);
    }

}
