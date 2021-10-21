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
     * <p>
     * Item may not be received, make sure to use canFitItem before calling this method.
     * </p>
     *
     * @param item The item to give.
     */
    void giveItem(IItem item);

    /**
     * If the item can be added.
     *
     * @param itemType The type of item to add.
     *
     * @return Whether the inventory has space left for the item or not.
     */
    boolean canFitItem(ItemType itemType);

}
