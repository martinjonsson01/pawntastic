package com.thebois.models.inventory.items;

/**
 * A generic item of a specific type.
 */
public class Item implements IItem {

    private final ItemType type;

    /**
     * Instantiates with a given type.
     *
     * @param type The type of the item.
     */
    public Item(final ItemType type) {
        this.type = type;
    }

    @Override
    public ItemType getType() {
        return type;
    }

    @Override
    public float getWeight() {
        return getType().getWeight();
    }

}
