package com.thebois.models.beings;

import java.io.Serializable;

import com.thebois.models.Position;

/**
 * An internal interface that represents a performer of actions.
 */
public interface IActionPerformer extends Serializable, IGiver, IReceiver {

    /**
     * Gives the performer a new destination.
     *
     * @param position Where to go.
     */
    void setDestination(Position position);

}
