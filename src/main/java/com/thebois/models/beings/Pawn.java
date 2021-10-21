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
     * @param role          The starting role.
     * @param inventory     The inventory of the pawn.
     */
    public Pawn(final Position startPosition, final AbstractRole role, final IInventory inventory) {
        super(startPosition, role, inventory);
    }

    @Override
    public void update(final float deltaTime) {
        super.update(deltaTime);
    }

}
