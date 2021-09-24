package com.thebois.controllers;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.beings.Colony;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.views.infoviews.ColonyInventoryView;

public class ColonyInventoryController {

    private final ColonyInventoryView colonyInventoryView;
    private final Colony colony;

    public ColonyInventoryController(ColonyInventoryView colonyInventoryView, Colony colony) {
        this.colonyInventoryView = colonyInventoryView;
        this.colony = colony;
    }

    public update() {
        final Map<ItemType, Integer> inventoryInfo = createInventoryInfoMap();
        colonyInventoryView.update(inventoryInfo);
    }

    private Map<ItemType, Integer> createInventoryInfoMap() {
        final Map<ItemType, Integer> inventoryInfo = new HashMap<ItemType, Integer>();
        for (ItemType itemType : ItemType.values()) {
            inventoryInfo.put(itemType, colony.getItemCount(itemType));
        }
    }

}
