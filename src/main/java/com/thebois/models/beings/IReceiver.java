package com.thebois.models.beings;

import com.thebois.models.IPositionable;
import com.thebois.models.inventory.items.IItem;

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

}
