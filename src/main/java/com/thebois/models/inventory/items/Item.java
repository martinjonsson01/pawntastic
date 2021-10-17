package com.thebois.models.inventory.items;

import java.util.Objects;

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
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null) return false;
        final Item item = (Item) o;
        return type == item.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

}
