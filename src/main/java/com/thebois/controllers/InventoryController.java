package com.thebois.controllers;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.views.infoviews.InventoryView;

/**
 * Makes sure the inventory view is updated with correct information.
 */
public class InventoryController {

    private final InventoryView inventoryView;
    private final IInventory colony;
    private Map<ItemType, Integer> itemCounts;

    /**
     * Instantiates the controller with the view and a colony reference.
     *
     * @param inventoryView The view to be updated.
     * @param colony              The colony to get information from, regarding the inventory.
     */
    public InventoryController(final InventoryView inventoryView,
                               final IInventory colony) {
        this.inventoryView = inventoryView;
        this.colony = colony;
        itemCounts = new HashMap<ItemType, Integer>();
    }

    /**
     * Updates the world with up-to-date information about the inventory.
     */
    public void update() {
        final Map<ItemType, Integer> newItemCounts = createItemCounts();
        if (!itemCounts.equals(newItemCounts)) {
            itemCounts = newItemCounts;
            inventoryView.update(itemCounts);
        }
    }

    private Map<ItemType, Integer> createItemCounts() {
        final Map<ItemType, Integer> newItemCounts = new HashMap<ItemType, Integer>();
        for (final ItemType itemType : ItemType.values()) {
            newItemCounts.put(itemType, colony.numberOf(itemType));
        }
        return newItemCounts;
    }

}
