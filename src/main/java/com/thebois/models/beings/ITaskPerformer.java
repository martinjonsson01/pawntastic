package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * An internal interface that represents a performer of tasks.
 */
public interface ITaskPerformer {

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

}
