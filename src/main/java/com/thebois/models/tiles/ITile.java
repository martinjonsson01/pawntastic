package com.thebois.models.tiles;

import com.thebois.models.Position;

/**
 * A section of the world representing a structure or resource.
 */
public interface ITile {

    /**
     * Gets the current location.
     *
     * @return The current location.
     */
    Position getPosition();

}
