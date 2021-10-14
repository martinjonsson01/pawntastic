package com.thebois.models.beings;

import com.thebois.models.Position;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.inventory.IInventory;

/**
 * A being that belongs to and is controlled by a colony.
 */
public class Pawn extends AbstractBeing {

    /**
     * Instantiates with an initial position and role.
     *
     * @param startPosition The initial position.
     * @param inventory     A storage place for items harvested.
     * @param role          The starting role.
     */
    public Pawn(final Position startPosition, final IInventory inventory, final AbstractRole role) {
        super(startPosition, inventory, role);
    }

}
