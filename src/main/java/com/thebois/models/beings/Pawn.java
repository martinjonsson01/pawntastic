package com.thebois.models.beings;

import java.util.Random;

import com.thebois.models.Position;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    private final Random random = new Random();

    /**
     * Creates a Pawn.
     */
    public Pawn() {
        final Position randomPosition = new Position(random.nextInt(50), random.nextInt(50));
        setDestination(randomPosition);
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

    @Override
    public IBeing deepClone() {
        return new Pawn(this.getPosition(), this.getDestination());
    }

}
