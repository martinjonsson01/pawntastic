package com.thebois.models.beings.roles;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.ResourceType;

/**
 * The lumberjack chops down trees to collect wood and store it in stockpiles.
 */
class LumberjackRole extends AbstractHarvesterRole {

    /**
     * Instantiates with a way of finding resources.
     *
     * @param finder The locator of resources.
     * @param world  The world in which the resources are located.
     */
    LumberjackRole(final IResourceFinder finder, final IWorld world) {
        super(finder, world, ResourceType.TREE);
    }

    @Override
    public RoleType getType() {
        return RoleType.LUMBERJACK;
    }

}
