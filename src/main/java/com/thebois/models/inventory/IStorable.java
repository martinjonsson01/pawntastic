package com.thebois.models.inventory;

import java.util.List;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Methods for storing items.
 *
 * @author Mathias
 */
public interface IStorable {

    /**
     * Checks whether the inventory is filled to its capacity.
     *
     * @return Whether more items can be added or not.
     */
    boolean isFull();

    /**
     * Tries to add an item to the inventory.
     *
     * @param item What to add to the inventory.
     *
     * @return Whether the item could fit or not.
     */
    boolean tryAdd(IItem item);

    /**
     * Allows multiple IItems to be added at the same time to the implemented inventory.
     *
     * @param stack The items to be added to the inventory.
     */
    void addMultiple(List<IItem> stack);

    /**
     * Checks if the type of item can fit inside inventory.
     *
     * @param itemType The type of item.
     *
     * @return Whether the item could be fitted or not.
     */
    boolean canFitItem(ItemType itemType);

}
