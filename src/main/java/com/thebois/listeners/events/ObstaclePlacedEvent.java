package com.thebois.listeners.events;

import com.thebois.models.Position;

/**
 * Describes an obstacle that has been placed in the world.
 */
public class ObstaclePlacedEvent {

    private final int x;
    private final int y;

    /**
     * Instantiates with a position for the new obstacle.
     *
     * @param x The x-coordinate of the new obstacle.
     * @param y The y-coordinate of the new obstacle.
     */
    public ObstaclePlacedEvent(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Position getPosition() {
        return new Position(getX(), getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
