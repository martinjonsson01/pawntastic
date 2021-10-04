package com.thebois.controllers.info;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.IController;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.views.info.IActorView;
import com.thebois.views.info.InventoryView;

/**
 * Makes sure the inventory view is updated with correct information.
 */
public class InventoryController implements IController<IActorView> {

    private final InventoryView inventoryView;
    private final IInventory inventory;
    private Map<ItemType, Integer> itemCounts;

    /**
     * Instantiates the controller with the view and a colony reference.
     *
     * @param inventory The inventory to get information from, e.g. item counts.
     * @param uiSkin    The skin to style widgets with.
     */
    public InventoryController(final IInventory inventory, final Skin uiSkin) {
        this.inventoryView = new InventoryView(uiSkin);
        this.inventory = inventory;
        itemCounts = new HashMap<ItemType, Integer>();
    }

    @Override
    public IActorView getView() {
        return inventoryView;
    }

    /**
     * Updates the inventoryView with up-to-date information about the inventory.
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
            newItemCounts.put(itemType, inventory.numberOf(itemType));
        }
        return newItemCounts;
    }

}
