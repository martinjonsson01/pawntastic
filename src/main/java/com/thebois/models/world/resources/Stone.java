package com.thebois.models.world.resources;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

/**
 * A large stone on the ground.
 *
 * @author Mathias
 */
class Stone extends AbstractResource {

    /**
     * The time it takes to harvest, in seconds.
     */
    private static final float HARVEST_TIME = 2f;

    /**
     * Instantiate at a given position.
     *
     * @param position The location to place it.
     */
    Stone(final Position position) {
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

    @Override
    public float getHarvestTime() {
        return HARVEST_TIME;
    }

}
