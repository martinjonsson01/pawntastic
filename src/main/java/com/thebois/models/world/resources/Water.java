package com.thebois.models.world.resources;

import com.thebois.models.Position;

/**
 * Resource of type water.
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
     * @param posX X coordinate for the water resource.
     * @param posY Y coordinate for the water resource.
     */
    public Water(final float posX, final float posY) {
        super(posX, posY);
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
