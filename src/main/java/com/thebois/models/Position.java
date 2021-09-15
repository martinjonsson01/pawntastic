package com.thebois.models;

/**
 * A two-dimensional location.
 */
public final class Position {

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

}
