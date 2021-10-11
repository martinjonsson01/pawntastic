package com.thebois.models.world.structures;

import java.util.Collection;

import com.thebois.models.inventory.items.IItem;

/**
 * IBuildable is an interface for interacting game entities that can be built.
 */
public interface IBuildable {

    /**
     * A collection of IItems that are needed for the completion of the IBuildable object.
     *
     * @return Returns a collection of needed items for construction.
     */
    Collection<IItem> neededItems();

    /**
     * Method for delivering an IItem to an IBuildable.
     *
     * @param deliveredItem the IItem to be delivered.
     *
     * @return Returns whether the delivery was successful or not.
     */
    boolean deliverItem(IItem deliveredItem);

    /**
     * Returns a value between 0 and 1 based on how many of the needed IItems have been received.
     *
     * @return Returns a ratio of completion.
     */
    float builtStatus();

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
    boolean dismantle(IItem retrieving);
}
