package com.thebois.models.world.structures;

import java.util.Collection;

import com.thebois.models.world.inventory.IItem;

/**
 * IBuildable is an interface for interacting game entities that can be built.
 */
public interface IBuildable {

    /**
     * A collection of IItems that are needed for the completion of the IBuildable object.
     *
     * @return collection of needed items for construction.
     */
    Collection<IItem> neededItems();

    /**
     * Method for delivering an IItem to an IBuildable.
     *
     * @param deliveredItem the IItem to be delivered.
     *
     * @return if delivery was successful.
     */
    boolean deliverItem(IItem deliveredItem);

    /**
     * Returns a value between 0 and 1 based on how many of the needed IItems have been received.
     *
     * @return ratio of completion.
     */
    float builtStatus();

    /**
     * Method for removing selected IItems from the IBuildable object.
     *
     * @param retrieving IItem to be retrieved from the object.
     *
     * @return if the retrieval was successful.
     */
    boolean dismantle(IItem retrieving);
}
