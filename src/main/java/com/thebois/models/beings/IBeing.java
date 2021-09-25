package com.thebois.models.beings;

import java.util.Collection;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.world.structures.IStructure;

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
     * Updates collection of known IStructures.
     *
     * @param structures New collection of IStructures.
     */
    void updateKnownStructures(Collection<IStructure> structures);

}
