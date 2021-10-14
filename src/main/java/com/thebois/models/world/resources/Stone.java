package com.thebois.models.world.resources;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

/**
 * A large stone on the ground.
 */
public class Stone extends AbstractResource {

    /**
     * Instantiate a new rock resource at given position.
     *
     * @param x X coordinate for the rock resource.
     * @param y Y coordinate for the rock resource.
     */
    public Stone(final float x, final float y) {
        this(new Position(x, y));
    }

    /**
     * Instantiate a new rock resource at given position.
     *
     * @param position The position where the rock resource should be created.
     */
    public Stone(final Position position) {
        super(position);
    }

    @Override
    public IResource deepClone() {
        return new Stone(getPosition());
    }

    @Override
    public float getCost() {
        return Float.MAX_VALUE;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.STONE;
    }

    @Override
    public IItem harvest() {
        return ItemFactory.fromType(ItemType.ROCK);
    }

}
