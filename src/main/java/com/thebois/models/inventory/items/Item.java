package com.thebois.models.inventory.items;

import java.io.Serializable;

/**
 * A generic item of a specific type.
 */
public class Item implements IItem, Serializable {

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

}
