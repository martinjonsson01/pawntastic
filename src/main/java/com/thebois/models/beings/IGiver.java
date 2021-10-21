package com.thebois.models.beings;

import com.thebois.models.IPositionable;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Something that can give out items.
 */
public interface IGiver extends IPositionable {

    /**
     * Take item of given type.
     * <p>
     * May return null if giver doesn't have item, make sure to call hasItem before using this
     * method.
     * </p>
     *
     * @param itemType The type of item to take.
     *
     * @return The item.
     */
    IItem takeItem(ItemType itemType);

    /**
     * If the giver has the item.
     *
     * @param itemType The type of item.
     *
     * @return Whether the giver has the item or not.
     */
    boolean hasItem(ItemType itemType);

}
