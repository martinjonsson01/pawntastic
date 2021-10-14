package com.thebois.models.inventory.items;

/**
 * Used in recipes/blueprints that require logs.
 */
class Log implements IItem {

    @Override
    public ItemType getType() {
        return ItemType.LOG;
    }

}
