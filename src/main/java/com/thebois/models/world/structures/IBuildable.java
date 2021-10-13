package com.thebois.models.world.structures;

import java.util.Collection;

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
     * Delivers an item to contribute to construction.
     *
     * @param deliveredItem The item to be delivered.
     *
     * @return Returns whether the delivery was successful or not.
     */
    boolean deliverItem(IItem deliveredItem);

    /**
     * Returns a value between 0 and 1 that denotes the ratio of completion.
     *
     * @return Returns a ratio of completion.
     */
    float getBuiltRatio();

    /**
     * Returns true if construction is completed.
     *
     * @return Returns whether the IBuildable is built or not.
     */
    boolean isCompleted();

    /**
     * Method for removing selected IItems from the IBuildable object.
     *
     * @param retrieving IItem to be retrieved from the object.
     *
     * @return Returns whether the retrieval was successful or not.
     */
    IItem dismantle(ItemType retrieving);
}
