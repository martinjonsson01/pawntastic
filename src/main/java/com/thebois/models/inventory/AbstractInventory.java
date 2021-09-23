package com.thebois.models.inventory;

import java.util.ArrayList;

/**
 * Base implementation for all inventories.
 */
public abstract class AbstractInventory {

    private final ArrayList<IItem> itemHolder = new ArrayList<>();

    /**
     * Items can be put into the inventory.
     *
     * @param item The item to be added to the inventory
     */
    public void addItem(final IItem item) {
        itemHolder.add(item);
    }

    /**
     * Removes and returns the first item of the specified type.
     *
     * @param itemType The item type to be taken
     *
     * @return The item removed from the inventory
     */
    public IItem takeItem(final ItemType itemType) {

        IItem item = null;
        if (doesItemTypeExist(itemType)) {
            final int firstIndexOfItemType = getFirstIndexOf(itemType);
            item = itemHolder.remove(firstIndexOfItemType);
        }
        return item;
    }

    private int getFirstIndexOf(final ItemType itemType) {
        int index = -1;
        for (final IItem item : itemHolder) {
            if (item.getType().equals(itemType)) {
                index = itemHolder.indexOf(item);
            }
        }
        return index;
    }

    /**
     * Check if an item type is in the current inventory.
     *
     * @param itemType The item type to be checked for
     *
     * @return A boolean indicating if the item is in the inventory or not
     */
    public boolean doesItemTypeExist(final ItemType itemType) {
        for (final IItem item : itemHolder) {
            if (item.getType().equals(itemType)) {
                return true;
            }
        }
        return false;
    }

}
