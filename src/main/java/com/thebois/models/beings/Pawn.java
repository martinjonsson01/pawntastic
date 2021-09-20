package com.thebois.models.beings;

import java.util.Random;

import com.thebois.models.Position;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    public Pawn(Random random) {
        super(random);
    }

    public Pawn(final Position currentPosition, final Position destination) {
        super(currentPosition, destination);
    }

}
