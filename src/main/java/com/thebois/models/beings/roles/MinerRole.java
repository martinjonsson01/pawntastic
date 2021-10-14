package com.thebois.models.beings.roles;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.ResourceType;

/**
 * The miner digs stone out of rocks.
 */
class MinerRole extends AbstractHarvesterRole {

    /**
     * Instantiates with a way of finding resources.
     *
     * @param finder The locator of resources.
     * @param world  The world in which the resources are located.
     */
    MinerRole(final IResourceFinder finder, final IWorld world) {
        super(finder, world, ResourceType.STONE);
    }

    @Override
    public RoleType getType() {
        return RoleType.MINER;
    }

}
