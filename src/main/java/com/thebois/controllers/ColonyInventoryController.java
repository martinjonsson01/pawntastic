package com.thebois.controllers;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.beings.Colony;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.views.infoviews.ColonyInventoryView;

/**
 * Makes sure the inventory view is updated with correct information.
 */
public class ColonyInventoryController {

    private final ColonyInventoryView colonyInventoryView;
    private final Colony colony;
    private Map<ItemType, Integer> inventoryInfo;

    /**
     * Instantiates the controller with the view and a colony reference.
     *
     * @param colonyInventoryView The view to be updated.
     * @param colony              The colony to get information from, regarding the inventory.
     */
    public ColonyInventoryController(ColonyInventoryView colonyInventoryView, Colony colony) {
        this.colonyInventoryView = colonyInventoryView;
        this.colony = colony;
        inventoryInfo = new HashMap<ItemType, Integer>();
    }

    /**
     * Updates the world with up to date information about the inventory.
     */
    public void update() {
        final Map<ItemType, Integer> newInventoryInfo = createInventoryInfoMap();
        if (!inventoryInfo.equals(newInventoryInfo)) {
            inventoryInfo = newInventoryInfo;
            colonyInventoryView.update(inventoryInfo);
        }
    }

    private Map<ItemType, Integer> createInventoryInfoMap() {
        final Map<ItemType, Integer> newInventoryInfo = new HashMap<ItemType, Integer>();
        for (final ItemType itemType : ItemType.values()) {
            newInventoryInfo.put(itemType, colony.getItemCount(itemType));
        }
        return newInventoryInfo;
    }

}
