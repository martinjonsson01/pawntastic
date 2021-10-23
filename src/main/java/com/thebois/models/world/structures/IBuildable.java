package com.thebois.models.world.structures;

import java.util.Collection;
import java.util.Optional;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Allows interaction with game entities that can be built.
 */
public interface IBuildable {

    /**
     * Returns a collection of the items that are needed for the completion.
     *
     * @return A collection of needed items for completion.
     */
    Collection<ItemType> getNeededItems();

    /**
     * Counts the amount of items still needed to build the structure.
     *
     * @param itemType The type of item.
     *
     * @return How many items of type is still needed.
     */
    int getNumberOfNeedItemType(ItemType itemType);

    /**
     * Delivers an item to contribute to construction.
     *
     * @param deliveredItem The item to be delivered.
     *
     * @return Returns whether the delivery was successful or not.
     */
    boolean tryDeliverItem(IItem deliveredItem);

    /**
     * Returns a value between 0 and 1 that denotes the ratio of completion.
     *
     * @return Returns a ratio of completion.
     */
    float getBuiltRatio();

    /**
     * Returns true if construction is completed.
     *
     * @return Returns whether construction is completed or not.
     */
    boolean isCompleted();

    /**
     * Method for removing specified item.
     *
     * @param retrieving Item to be retrieved from the object.
     *
     * @return Returns the retrieved item.
     */
    Optional<IItem> tryDismantle(ItemType retrieving);

}
