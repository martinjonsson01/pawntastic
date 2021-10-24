package com.thebois.models.beings;

import java.io.Serializable;

import com.thebois.abstractions.IPositionable;
import com.thebois.models.Position;
import com.thebois.models.inventory.IStorable;
import com.thebois.models.inventory.ITakeable;

/**
 * An internal interface that represents a performer of actions.
 *
 * @author Martin
 */
public interface IActionPerformer extends Serializable, IStorable, ITakeable, IPositionable {

    /**
     * Gives the performer a new destination.
     *
     * @param position Where to go.
     */
    void setDestination(Position position);

}
