package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    /**
     * Creates a Pawn.
     */
    public Pawn() {
    }

    /**
     * Creates a pawn with a initial position and a destination to travel to.
     *
     * @param currentPosition initial position.
     * @param destination     initial destination to travel to.
     */
    public Pawn(final Position currentPosition, final Position destination) {
        super(currentPosition, destination);
    }

}
