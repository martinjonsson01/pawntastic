package com.thebois.models.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Common method for all the uses an inventory or is an inventory.
 */
public interface IInventory extends Serializable {

    /**
     * Allows an IItem to be added to the implemented inventory.
     *
     * @param item The item to be added to the inventory.
     */
    void add(IItem item);

    /**
     * Allows multiple IItems to be added at the same time to the implemented inventory.
     *
     * @param stack The items to be added to the inventory.
     */
    void addMultiple(List<IItem> stack);

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

}
