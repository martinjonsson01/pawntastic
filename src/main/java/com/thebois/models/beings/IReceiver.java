package com.thebois.models.beings;

import com.thebois.models.IPositionable;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Something that can receive items.
 */
public interface IReceiver extends IPositionable {

    /**
     * Try to give the receiver an item.
     *
     * @param item The item to give.
     *
     * @return Whether the item could be given or not.
     */
    boolean tryGiveItem(IItem item);

    /**
     * If the item can be added.
     *
     * @param itemType The type of item to add.
     *
     * @return Whether the inventory has space left for the item or not.
     */
    boolean canFitItem(ItemType itemType);

}
