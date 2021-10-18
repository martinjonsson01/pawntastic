package com.thebois.models.inventory.items;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null) return false;
        final Item item = (Item) other;
        return type == item.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

}
