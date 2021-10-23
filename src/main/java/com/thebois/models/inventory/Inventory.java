package com.thebois.models.inventory;

import java.util.ArrayList;
import java.util.List;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

/**
 * Base implementation for all inventories.
 */
public class Inventory implements IInventory {

    private final ArrayList<IItem> items = new ArrayList<>();
    private final float maxCapacity;

    /**
     * Instantiates with infinite capacity.
     */
    public Inventory() {
        this.maxCapacity = Float.MIN_VALUE;
    }

    /**
     * Instantiates with a given maximum carrying capacity.
     *
     * @param maxCapacity The maximum weight, in kilograms, that can be contained.
     */
    public Inventory(final float maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public boolean isFull() {
        final float weight = calculateTotalWeight();
        return atMaxCapacity(weight);
    }

    private float calculateTotalWeight() {
        return items.stream().map(IItem::getWeight).reduce(0f, Float::sum);
    }

    private boolean atMaxCapacity(final float weight) {
        if (hasInfiniteCapacity()) return false;
        return weight >= maxCapacity;
    }

    private boolean hasInfiniteCapacity() {
        return maxCapacity == Float.MIN_VALUE;
    }

    @Override
    public boolean tryAdd(final IItem item) {
        final float newWeight = calculateTotalWeight() + item.getWeight();
        if (isAboveCapacity(newWeight)) return false;

        items.add(item);
        return true;
    }

    @Override
    public void addMultiple(final List<IItem> stack) {
        for (final IItem item : stack) {
            tryAdd(item);
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

    @Override
    public boolean isEmpty() {
        return calculateTotalWeight() == 0f;
    }

    @Override
    public IItem takeFirstItem() {
        return items.remove(items.size() - 1);
    }

    @Override
    public boolean canFitItem(final ItemType itemType) {
        final IItem item = ItemFactory.fromType(itemType);
        final float weight = calculateTotalWeight();
        return !isAboveCapacity(item.getWeight() + weight);
    }

    private boolean isAboveCapacity(final float weight) {
        if (hasInfiniteCapacity()) return false;
        return weight > maxCapacity;
    }

}
