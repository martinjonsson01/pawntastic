package com.thebois.abstractions;

import java.util.Optional;

import com.thebois.models.Position;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;

/**
 * Represents a locator of resources.
 */
public interface IResourceFinder {

    /**
     * Finds and returns a resource that is nearby the given position and of the given type.
     *
     * @param origin Where to look for resources nearby.
     * @param type   What kind of resource to look for.
     *
     * @return The nearby resource of the given type, otherwise empty.
     */
    Optional<IResource> getNearbyOfType(Position origin, ResourceType type);

}
