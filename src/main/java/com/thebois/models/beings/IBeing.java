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
     *
     * @param deltaTime The amount of time that has passed since the last update, in seconds.
     */
    void update(float deltaTime);

    /**
     * Gets a copy of the path the being is currently walking.
     *
     * @return The positions in the path.
     */
    Iterable<Position> getPath();

    /**
     * Returns true if the being is alive and false if it's dead.
     *
     * @return Whether the being is alive or not.
     */
    boolean isAlive();

    /**
     * Gets the health ratio, where 0 is dead and 1 is full health.
     * <p>
     * The Health ratio is current health divided by max health.
     * </p>
     *
     * @return The current ratio a number between 1 and 0.
     */
    float getHealthRatio();

}
