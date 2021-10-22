package com.thebois.models.beings.roles;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.ResourceType;

/**
 * The fisher catches fish out of water.
 *
 * @author Martin
 */
class FisherRole extends AbstractHarvesterRole {

    /**
     * Instantiates with a way of finding resources.
     *
     * @param finder The locator of resources.
     * @param world  The world in which the resources are located.
     */
    FisherRole(final IResourceFinder finder, final IWorld world) {
        super(finder, world, ResourceType.WATER);
    }

    @Override
    public RoleType getType() {
        return RoleType.FISHER;
    }

}
