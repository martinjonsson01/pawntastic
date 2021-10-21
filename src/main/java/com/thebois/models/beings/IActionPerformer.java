package com.thebois.models.beings;

import java.io.Serializable;

import com.thebois.models.IPositionable;
import com.thebois.models.Position;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.inventory.ITakeable;

/**
 * An internal interface that represents a performer of actions.
 */
public interface IActionPerformer extends Serializable, IStoreable, ITakeable, IPositionable {

    /**
     * Gives the performer a new destination.
     *
     * @param position Where to go.
     */
    void setDestination(Position position);

}
