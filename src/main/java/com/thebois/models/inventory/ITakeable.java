package com.thebois.models.inventory;

import java.util.ArrayList;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Methods for taking items and checking if they exists.
 */
public interface ITakeable {

    /**
     * Removes and returns the first item of the specified type from the implemented inventory.
     *
     * @param itemType The item type to be taken.
     *
     * @return The item removed from the inventory.
     *
     * @throws IllegalArgumentException If the item type is not in the inventory.
     */
    IItem take(ItemType itemType);

    /**
     * Allows for multiple items to be taken at once from the implemented inventory.
     *
     * @param itemType The item type to be taken.
     * @param amount   The amount of items to be taken.
     *
     * @return The list of items removed from the inventory.
     *
     * @throws IllegalArgumentException If the amount given is greater that the items of the
     *                                  specified item type in the inventory.
     */
    ArrayList<IItem> takeAmount(ItemType itemType, int amount);

    /**
     * Check if an item type is in the implemented inventory.
     *
     * @param itemType The item type to be checked for.
     *
     * @return A boolean indicating if the item is in the inventory or not.
     */
    boolean hasItem(ItemType itemType);

    /**
     * Check if a given amount of an item type is in the implemented inventory.
     *
     * @param itemType The item type to be checked for.
     * @param amount   The amount of to be checked for.
     *
     * @return A boolean indicating it the amount of the specified item is in the inventory or not.
     */
    boolean hasItem(ItemType itemType, int amount);

    /**
     * Counts how many items are in the implemented inventory of the specified type.
     *
     * @param itemType The type to be counted.
     *
     * @return The counter.
     */
    int numberOf(ItemType itemType);

    /**
     * Checks if the inventory is empty.
     *
     * @return Whether the inventory is empty or not.
     */
    boolean isEmpty();

    /**
     * Gets the top most item.
     *
     * @return The item to take.
     */
    IItem takeNextItem();

}
