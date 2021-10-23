package com.thebois.models.world.structures;

import java.util.EnumMap;
import java.util.Map;

import com.thebois.models.Position;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.ItemType;

/**
 * A structure used for storing items.
 *
 * @author Jonathan
 */
class Stockpile extends AbstractStructure {

    private final IInventory inventory;

    Stockpile(final Position position, final IInventory inventory) {
        super(position, StructureType.STOCKPILE, getBuildMaterials());
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

}
