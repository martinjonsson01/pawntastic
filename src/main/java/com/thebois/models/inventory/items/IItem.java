package com.thebois.models.inventory.items;

/**
 * Items are objects that can be stored in inventories.
 *
 * @author Jonathan
 */
public interface IItem {

    /**
     * Returns the specific type of item.
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
