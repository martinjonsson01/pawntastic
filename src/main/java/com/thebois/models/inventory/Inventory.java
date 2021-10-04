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

    @Override
    public void add(final IItem item) {
        items.add(item);
    }

    @Override
    public void addMultiple(final List<IItem> stack) {
        for (final IItem item : stack) {
            add(item);
        }
    }

    @Override
    public IItem take(final ItemType itemType) {
        final int firstIndexOfItemType = getFirstIndexOf(itemType);
        if (firstIndexOfItemType != -1) {
            return items.remove(firstIndexOfItemType);
        }
        else {
            throw new IllegalArgumentException("Specified ItemType not in inventory");
        }
    }

    @Override
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
                break;
            }
        }
        return index;
    }

    @Override
    public boolean hasItem(final ItemType itemType) {
        return numberOf(itemType) >= 1;
    }

    @Override
    public boolean hasItem(final ItemType itemType, final int amount) {
        return numberOf(itemType) >= amount;
    }

    @Override
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
