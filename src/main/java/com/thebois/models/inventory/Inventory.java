package com.thebois.models.inventory;

import java.util.ArrayList;
import java.util.List;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Base implementation for all inventories.
 */
public class Inventory implements IInventory {

    private final ArrayList<IItem> items = new ArrayList<>();

    /**
     * Inputs an item to the inventory.
     *
     * @param item The item to be added to the inventory.
     */
    public void add(final IItem item) {
        items.add(item);
    }

    /**
     * Inputs more than just one item into the inventory.
     *
     * @param stack The items to be added to the inventory.
     */
    public void addMultiple(final List<IItem> stack) {
        for (final IItem item : stack) {
            add(item);
        }
    }

    /**
     * Removes and returns the first item of the specified type.
     *
     * @param itemType The item type to be taken.
     *
     * @return The item removed from the inventory.
     *
     * @throws IllegalArgumentException If the item type is not in the inventory.
     */
    public IItem take(final ItemType itemType) {

        if (hasItem(itemType)) {
            final int firstIndexOfItemType = getFirstIndexOf(itemType);
            return items.remove(firstIndexOfItemType);
        }
        else {
            throw new IllegalArgumentException("Specified ItemType not in inventory");
        }
    }

    /**
     * Take multiple items from the inventory.
     *
     * @param itemType The item type to be taken.
     * @param amount   The amount of items to be taken.
     *
     * @return The list of items removed from the inventory.
     *
     * @throws IllegalArgumentException If the amount given is greater that the items of the
     *                                  specified item type in the inventory.
     */
    public ArrayList<IItem> takeAmount(final ItemType itemType, final int amount) {
        final ArrayList<IItem> taken = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            try {
                taken.add(take(itemType));
            }
            catch (final IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                    "Not enough of the specified ItemType in the inventory");
            }
        }
        return taken;
    }

    private int getFirstIndexOf(final ItemType itemType) {
        int index = -1;
        for (final IItem item : items) {
            if (item.getType().equals(itemType)) {
                index = items.indexOf(item);
            }
        }
        return index;
    }

    /**
     * Check if an item type is in the current inventory.
     *
     * @param itemType The item type to be checked for.
     *
     * @return A boolean indicating if the item is in the inventory or not.
     */
    public boolean hasItem(final ItemType itemType) {
        for (final IItem item : items) {
            if (item.getType().equals(itemType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a given amount of an item type is in the current inventory.
     *
     * @param itemType The item type to be checked for.
     * @param amount   The amount of to be checked for.
     *
     * @return A boolean indicating it the amount of the specified item is in the inventory or not.
     */
    public boolean hasItem(final ItemType itemType, final int amount) {
        for (int i = 0; i <= amount; i++) {
            if (!hasItem(itemType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts how many items are in the current inventory of the specified type.
     *
     * @param itemType The type to be counted.
     *
     * @return The counter.
     */
    public int numberOf(final ItemType itemType) {
        int count = 0;
        for (final IItem item : items) {
            if (item.getType().equals(itemType)) {
                count++;
            }
        }
        return count;
    }

}
