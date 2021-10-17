package com.thebois.models.inventory.items;

/**
 * Items are objects that can be stored in inventories.
 */
public interface IItem {

    /**
     * Returns the speceifc type of item.
     *
     * @return The item type.
     */
    ItemType getType();

    /**
     * Gets the mass, described in kilograms.
     *
     * @return The mass in kilograms.
     */
    float getWeight();

}
