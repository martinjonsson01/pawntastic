package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * An entity.
 */
public interface IBeing {

    /**
     * Gets the current location.
     *
     * @return The current location.
     */
    Position getPosition();

    /**
     * Sets a position to move towards.
     * @param destination the desired position to move towards.
     */
    void walkTo(Position destination);

    /**
     * Updates the objects internal state.
     */
    void update();
}
