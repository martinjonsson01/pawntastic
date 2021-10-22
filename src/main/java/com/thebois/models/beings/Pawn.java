package com.thebois.models.beings;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;

/**
 * A being that belongs to and is controlled by a colony.
 */
public class Pawn extends AbstractBeing {

    /**
     * Instantiates with an initial position and role.
     *
     * @param startPosition The initial position.
     * @param role          The starting role.
     * @param hungerRole    The role to perform when hungry.
     */
    public Pawn(
        final Position startPosition,
        final AbstractRole role,
        final AbstractRole hungerRole) {
        super(startPosition, role, hungerRole);
    }

}
