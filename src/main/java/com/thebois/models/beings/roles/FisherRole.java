package com.thebois.models.beings.roles;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.ResourceType;

/**
 * The fisher catches fish out of water.
 */
class FisherRole extends AbstractHarvesterRole {

    /**
     * Instantiates with a way of finding resources.
     *
     * @param finder          The locator of resources.
     * @param structureFinder The locator of structures.
     * @param world           The world in which the resources are located.
     */
    FisherRole(
        final IResourceFinder finder, final IStructureFinder structureFinder, final IWorld world) {
        super(finder, structureFinder, world, ResourceType.WATER);
    }

    @Override
    public RoleType getType() {
        return RoleType.FISHER;
    }

}
