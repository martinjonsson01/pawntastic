package com.thebois.models.world;

import java.io.Serializable;

import com.thebois.models.IPositionable;

/**
 * A section of the world representing a structure or resource.
 */
public interface ITile extends Serializable, IPositionable {

    /**
     * Gets the cost of moving across.
     *
     * @return The cost of moving across
     */
    float getCost();

}
