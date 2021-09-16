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
     *
     * @param position the desired destination to move towards.
     */
    void walkTo(Position position);

    /**
     * Updates the objects internal state.
     */
    void update();

}
