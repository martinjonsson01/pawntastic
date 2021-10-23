package com.thebois.models.beings;

import com.thebois.listeners.IEventBusSource;
import com.thebois.listeners.events.SpawnPawnEvent;
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
     * @param startPosition  The initial position.
     * @param role           The starting role.
     * @param hungerRole     The role to perform when hungry.
     * @param eventBusSource A way of getting an event bus to listen for events on.
     * @param inventory      The inventory of the pawn.
     */
    public Pawn(
        final Position startPosition,
        final AbstractRole role,
        final AbstractRole hungerRole,
        final IEventBusSource eventBusSource,
        final IInventory inventory) {
        super(startPosition, role, hungerRole, inventory);
        eventBusSource.getEventBus().post(new SpawnPawnEvent());
    }

}
