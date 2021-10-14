package com.thebois.models.world.resources;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

/**
 * Resource of type water.
 */
public class Water extends AbstractResource {

    /**
     * Instantiate a new water resource at given position.
     *
     * @param x X coordinate for the water resource.
     * @param y Y coordinate for the water resource.
     */
    public Water(final float x, final float y) {
        this(new Position(x, y));
    }

    /**
     * Instantiate a new water resource at given position.
     *
     * @param position The position where the water resource should be created.
     */
    public Water(final Position position) {
        super(position);
    }

    @Override
    public ResourceType getType() {
        return ResourceType.WATER;
    }

    @Override
    public IItem harvest() {
        return ItemFactory.fromType(ItemType.FISH);
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
