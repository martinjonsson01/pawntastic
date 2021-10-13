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
     *
     * @throws UnsupportedOperationException If the type of resource wanted to be created have not
     *                                       been implemented in the factory.
     */
    public static IResource createResource(final ResourceType type, final int x, final int y) {
        if (type == ResourceType.WATER) {
            return new Water(x, y);
        }
        else if (type == ResourceType.TREE) {
            return new Tree(x, y);
        }
        else if (type == ResourceType.ROCK) {
            return new Rock(x, y);
        }
        throw new UnsupportedOperationException();
    }

}
