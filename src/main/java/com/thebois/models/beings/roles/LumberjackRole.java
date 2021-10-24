package com.thebois.models.beings.roles;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.resources.ResourceType;

/**
 * The lumberjack chops down trees to collect wood and store it in stockpiles.
 *
 * @author Martin
 */
class LumberjackRole extends AbstractHarvesterRole {

    /**
     * Instantiates with a way of finding resources.
     *
     * @param resourceFinder  The locator of resources.
     * @param structureFinder The locator of structures.
     * @param world           The world in which the resources are located.
     */
    LumberjackRole(
        final IResourceFinder resourceFinder,
        final IStructureFinder structureFinder,
        final IWorld world) {
        super(resourceFinder, structureFinder, world, ResourceType.TREE);
    }

    @Override
    public RoleType getType() {
        return RoleType.LUMBERJACK;
    }

}
