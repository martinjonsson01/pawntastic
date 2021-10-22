package com.thebois.models.world.resources;

/**
 * Factory used to create resources.
 *
 * @author Mathias
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
     *
     * @throws NullPointerException If the type of resource is null.
     */
    public static IResource createResource(final ResourceType type, final int x, final int y) {
        return switch (type) {
            case STONE -> new Stone(x, y);
            case WATER -> new Water(x, y);
            case TREE -> new Tree(x, y);
        };
    }

}
