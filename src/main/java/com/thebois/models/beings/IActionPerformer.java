package com.thebois.models.beings;

import java.io.Serializable;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;

/**
 * An internal interface that represents a performer of actions.
 */
public interface IActionPerformer extends Serializable {

    /**
     * Gives the performer a new destination.
     *
     * @param position Where to go.
     */
    void setDestination(Position position);

    /**
     * Gets the current location.
     *
     * @return The current location
     */
    Position getPosition();

    /**
     * Gives an item.
     *
     * @param item The item to give.
     */
    void addItem(IItem item);

}
