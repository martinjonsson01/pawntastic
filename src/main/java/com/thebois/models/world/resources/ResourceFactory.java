package com.thebois.models.world.resources;

import com.thebois.models.Position;

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
     * @param position The position of the resource.
     *
     * @return The created resource.
     *
     * @throws NullPointerException If the type of resource is null.
     */
    public static IResource createResource(final ResourceType type, final Position position) {
        return switch (type) {
            case STONE -> new Stone(position);
            case WATER -> new Water(position);
            case TREE -> new Tree(position);
        };
    }

    /**
     * Creates an instance of a resource with given type at the given position.
     *
     * @param type The type of resource to be created.
     * @param x    The X position of the resource.
     * @param y    The Y position of the resource.
     *
     * @return The created resource.
     */
    public static IResource createResource(final ResourceType type, final int x, final int y) {
        return createResource(type, new Position(x, y));
    }

}
