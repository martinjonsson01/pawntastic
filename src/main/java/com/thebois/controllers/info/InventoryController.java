package com.thebois.controllers.info;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.IController;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.Stockpile;
import com.thebois.models.world.structures.StructureType;
import com.thebois.views.info.IActorView;
import com.thebois.views.info.InventoryView;

/**
 * Makes sure the inventory view is updated with correct information.
 */
public class InventoryController implements IController<IActorView> {

    private final InventoryView inventoryView;
    private final World world;
    private Map<ItemType, Integer> itemCounts;

    /**
     * Instantiates the controller with the view and a colony reference.
     *
     * @param world  The world to gather all stockpiles and compile the inventory item counts from.
     * @param uiSkin The skin to style widgets with.
     */
    public InventoryController(final World world, final Skin uiSkin) {
        this.inventoryView = new InventoryView(uiSkin);
        this.world = world;
        itemCounts = new HashMap<ItemType, Integer>();
    }

    @Override
    public IActorView getView() {
        return inventoryView;
    }

    @Override
    public void update() {
        final Map<ItemType, Integer> newItemCounts = createItemCounts();
        if (!itemCounts.equals(newItemCounts)) {
            itemCounts = newItemCounts;
            inventoryView.update(itemCounts);
        }
    }

    private Map<ItemType, Integer> createItemCounts() {
        final Map<ItemType, Integer> newItemCounts = new HashMap<ItemType, Integer>();
        final Stockpile[] stockpiles = createStockpileArray();
        for (final Stockpile stockpile : stockpiles) {
            final IInventory inventory = stockpile.getInventory();
            for (final ItemType itemType : ItemType.values()) {

                newItemCounts.put(itemType, inventory.numberOf(itemType));
            }
            // Break with the current implementation because all stockpile inventories are the same.
            // In the future where inventories are unique, only small changes are needed.
            break;
        }

        return newItemCounts;
    }

    private Stockpile[] createStockpileArray() {
        return world.getStructures().stream().filter(structure -> structure
            .getType()
            .equals(StructureType.STOCKPILE)).toArray(Stockpile[]::new);
    }

}
