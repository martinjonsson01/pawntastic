package com.thebois.models.world.resources;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;

/**
 * Resource of type rock.
 */
public class Rock extends AbstractResource {

    /**
     * Instantiate a new rock resource at given position.
     *
     * @param position The position where the rock resource should be created.
     */
    public Rock(final Position position) {
        super(position);
    }

    /**
     * Instantiate a new rock resource at given position.
     *
     * @param x X coordinate for the rock resource.
     * @param y Y coordinate for the rock resource.
     */
    public Rock(final float x, final float y) {
        this(new Position(x, y));
    }

    @Override
    public IResource deepClone() {
        return new Rock(getPosition());
    }

    @Override
    public float getCost() {
        return Float.MAX_VALUE;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.ROCK;
    }

    @Override
    public IItem harvest() {
        return null;
    }

}
