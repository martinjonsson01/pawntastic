package com.thebois.models.world.resources;

import com.thebois.abstractions.IDeepClonable;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.ITile;

/**
 * Represents a specific resource. E.g. Trees, Rocks etc.
 *
 * @author Mathias
 */
public interface IResource extends ITile, IDeepClonable<IResource> {

    /**
     * Gets the resource type.
     *
     * @return The type of resource.
     */
    ResourceType getType();

    /**
     * Harvests the resource.
     *
     * @return The harvested item.
     */
    IItem harvest();

    /**
     * Gets the time it takes to completely harvest, in seconds.
     *
     * @return How many seconds it takes to harvest one item.
     */
    float getHarvestTime();

}
