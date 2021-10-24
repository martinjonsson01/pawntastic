package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.thebois.models.Position;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * A structure used for storing items.
 */
class Stockpile extends AbstractStructure implements IInventory {

    private final IInventory inventory;

    Stockpile(final Position position, final IInventory inventory) {
        super(position, StructureType.STOCKPILE, getBuildMaterials());
        this.inventory = inventory;
    }

    /**
     * Instantiate with a given position, structure type, materials needed for building and and
     * storage inventory.
     *
     * @param position       Where the structure is placed.
     * @param structureType  What kind of structure.
     * @param buildMaterials Needed materials for building the structure
     * @param inventory      The storage inventory.
     */
    protected Stockpile(
        final Position position,
        final StructureType structureType,
        final Map<ItemType, Integer> buildMaterials,
        final IInventory inventory) {
        super(position, structureType, buildMaterials);
        this.inventory = inventory;
    }

    private static Map<ItemType, Integer> getBuildMaterials() {
        final Map<ItemType, Integer> neededItems = new EnumMap<>(ItemType.class);

        final int numberOfItems = 4;

        neededItems.put(ItemType.LOG, numberOfItems);
        neededItems.put(ItemType.ROCK, numberOfItems);

        return neededItems;
    }

    @Override
    protected float getCostWhenBuilt() {
        return Float.MAX_VALUE;
    }

    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isFull() {
        return inventory.isFull();
    }

    @Override
    public boolean tryAdd(final IItem item) {
        return inventory.tryAdd(item);
    }

    @Override
    public void addMultiple(final List<IItem> stack) {
        inventory.addMultiple(stack);
    }

    @Override
    public boolean canFitItem(final ItemType itemType) {
        return inventory.canFitItem(itemType);
    }

    @Override
    public IItem take(final ItemType itemType) {
        return inventory.take(itemType);
    }

    @Override
    public ArrayList<IItem> takeAmount(final ItemType itemType, final int amount) {
        return inventory.takeAmount(itemType, amount);
    }

    @Override
    public boolean hasItem(final ItemType itemType) {
        return inventory.hasItem(itemType);
    }

    @Override
    public boolean hasItem(final ItemType itemType, final int amount) {
        return inventory.hasItem(itemType, amount);
    }

    @Override
    public int numberOf(final ItemType itemType) {
        return inventory.numberOf(itemType);
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemType getItemTypeOfAnyItem() {
        return inventory.getItemTypeOfAnyItem();
    }

}
