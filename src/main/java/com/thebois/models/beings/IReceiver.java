package com.thebois.models.beings;

import com.thebois.models.inventory.items.ItemType;

/**
 * Something that can receive items.
 */
public interface IReceiver {

    /**
     * Try to give the receiver an item.
     *
     * @param itemType The type of item to give.
     *
     * @return Whether the item could be given or not.
     */
    boolean tryGiveItem(ItemType itemType);

}
