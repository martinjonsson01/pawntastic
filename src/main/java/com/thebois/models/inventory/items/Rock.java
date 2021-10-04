package com.thebois.models.inventory.items;

/**
 * Used in recipes/blueprints that require logs.
 */
public class Rock implements IItem {

    @Override
    public ItemType getType() {
        return ItemType.ROCK;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Rock;
    }

}
