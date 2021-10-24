package com.thebois.models.beings.roles;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.ResourceType;

/**
 * The miner digs stone out of rocks.
 *
 * @author Martin
 */
class MinerRole extends AbstractHarvesterRole {

    /**
     * Instantiates with a way of finding resources.
     *
     * @param resourceFinder  The locator of resources.
     * @param structureFinder The locator of structures.
     * @param world           The world in which the resources are located.
     */
    MinerRole(
        final IResourceFinder resourceFinder,
        final IStructureFinder structureFinder,
        final IWorld world) {
        super(resourceFinder, structureFinder, world, ResourceType.STONE);
    }

    @Override
    public RoleType getType() {
        return RoleType.MINER;
    }

}
