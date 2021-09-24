package com.thebois.models.world.resources;

/**
 * Represents a specific resource. E.g. Trees, Rocks etc.
 */
public interface IResource {

    /**
     * Get the resource type.
     *
     * @return The Resource type for the resource.
     */
    ResourceType getType();

}
