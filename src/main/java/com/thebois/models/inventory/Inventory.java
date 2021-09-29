package com.thebois.models.inventory;

import java.util.ArrayList;
import java.util.Optional;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Base implementation for all inventories.
 */
public class Inventory {

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
     * Removes and returns the first item of the specified type.
     *
     * @param itemType The item type to be taken.
     *
     * @return The item removed from the inventory.
     */
    public Optional<IItem> take(final ItemType itemType) {

        Optional<IItem> item = Optional.empty();
        if (hasItem(itemType)) {
            final int firstIndexOfItemType = getFirstIndexOf(itemType);
            item = Optional.ofNullable(items.remove(firstIndexOfItemType));
        }
        return item;
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
    private boolean hasItem(final ItemType itemType) {
        for (final IItem item : items) {
            if (item.getType().equals(itemType)) {
                return true;
            }
        }
        return false;
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
