package com.thebois.models.world.resources;

/**
 * Factory used to create resources.
 */
public final class ResourceFactory {

    private ResourceFactory() {

    }

    /**
     * Generate a resource with given type at given position.
     *
     * @param type What type of resource.
     * @param x    X position for resource.
     * @param y    Y position for resource.
     *
     * @return The created resource.
     */
    public static IResource createResource(final ResourceType type, final int x, final int y) {
        if (type == ResourceType.WATER) {
            return new Water(x, y);
        }
        else {
            return new Tree(x, y);
        }
    }

}