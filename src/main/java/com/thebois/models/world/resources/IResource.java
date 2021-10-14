package com.thebois.models.world.resources;

import com.thebois.abstractions.IDeepClonable;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.world.ITile;

/**
 * Represents a specific resource. E.g. Trees, Rocks etc.
 */
public interface IResource extends ITile, IDeepClonable<IResource> {

    /**
     * Get the resource type.
     *
     * @return The Resource type for the resource.
     */
    ResourceType getType();

    /**
     * Harvests the resource.
     *
     * @return The harvested item.
     */
    IItem harvest();

}
