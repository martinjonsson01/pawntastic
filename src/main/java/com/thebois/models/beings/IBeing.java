package com.thebois.models.beings;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.Role;

/**
 * An entity.
 */
public interface IBeing {

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
    Role getRole();

    /**
     * Sets the role.
     *
     * @param role The role
     */
    void setRole(Role role);

}
