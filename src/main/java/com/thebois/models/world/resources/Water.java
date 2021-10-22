package com.thebois.models.world.resources;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

/**
 * Resource of type water.
 */
class Water extends AbstractResource {

    /**
     * The time it takes to harvest, in seconds.
     */
    private static final float HARVEST_TIME = 3f;

    /**
     * Instantiate a new water resource at given position.
     *
     * @param position The position where the water resource should be created.
     */
    Water(final Position position) {
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
    public float getHarvestTime() {
        return HARVEST_TIME;
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
