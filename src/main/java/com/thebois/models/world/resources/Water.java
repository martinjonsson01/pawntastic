package com.thebois.models.world.resources;

import com.thebois.models.Position;

/**
 * Resource of type water.
 *
 * @author Mathias
 */
public class Water extends AbstractResource {

    /**
     * Instantiate a new water resource at given position.
     *
     * @param position The position where the water resource should be created.
     */
    public Water(final Position position) {
        super(position);
    }

    /**
     * Instantiate a new water resource at given position.
     *
     * @param x X coordinate for the water resource.
     * @param y Y coordinate for the water resource.
     */
    public Water(final float x, final float y) {
        this(new Position(x, y));
    }

    @Override
    public ResourceType getType() {
        return ResourceType.WATER;
    }

    @Override
    public IResource deepClone() {
        return new Water(getPosition());
    }

    @Override
    public float getCost() {
        return Float.MAX_VALUE;
    }

}
