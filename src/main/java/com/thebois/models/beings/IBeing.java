package com.thebois.models.beings;

import java.io.Serializable;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;

/**
 * An entity.
 */
public interface IBeing extends Serializable {

    /**
     * Gets the current location.
     *
     * @return The current location
     */
    Position getPosition();

    /**
     * Gets the role.
     *
     * @return The role
     */
    AbstractRole getRole();

    /**
     * Sets the role.
     *
     * @param role The role
     */
    void setRole(AbstractRole role);

    /**
     * Updates the objects internal state.
     */
    void update();

    /**
     * Gets the current destination.
     *
     * @return The destination location.
     */
    Position getDestination();

}
