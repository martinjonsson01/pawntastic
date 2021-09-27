package com.thebois.models.inventory.items;

/**
 * Items are objects that can be stored in inventories.
 */
public interface IItem {

    /**
     * Returns the speceifc type of item.
     *
     * @return The item type
     */
    ItemType getType();

}