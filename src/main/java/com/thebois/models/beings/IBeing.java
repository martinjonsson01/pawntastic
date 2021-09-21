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
     * Updates the objects internal state.
     */
    void update();

}
