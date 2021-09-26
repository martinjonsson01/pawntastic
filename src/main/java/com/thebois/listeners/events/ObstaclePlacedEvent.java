package com.thebois.listeners.events;

import com.thebois.models.Position;

/**
 * Describes an obstacle that has been placed in the world.
 */
public class ObstaclePlacedEvent {

    private final int posX;
    private final int posY;

    /**
     * Instantiates with a position for the new obstacle.
     *
     * @param posX The x-coordinate of the new obstacle.
     * @param posY The y-coordinate of the new obstacle.
     */
    public ObstaclePlacedEvent(final int posX, final int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Position getPosition() {
        return new Position(getPosX(), getPosY());
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}
