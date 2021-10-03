package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * A being that belongs to and is controlled by a colony.
 */
public class Pawn extends AbstractBeing {

    /**
     * Instantiates with an initial position.
     *
     * @param startPosition The initial position.
     */
    public Pawn(final Position startPosition) {
        super(startPosition);
    }

}
