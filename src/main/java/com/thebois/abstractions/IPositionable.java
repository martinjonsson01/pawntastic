package com.thebois.abstractions;

import com.thebois.models.Position;

/**
 * Something that has a position.
 *
 * @author Mathias
 */
public interface IPositionable {

    /**
     * Gets the current location.
     *
     * @return The current location.
     */
    Position getPosition();

}
