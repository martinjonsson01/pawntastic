package com.thebois.models.inventory.items;

/**
 * Used in recipes/blueprints that require logs.
 */
public class Rock implements IItem {

    @Override
    public ItemType getType() {
        return ItemType.ROCK;
    }

}
