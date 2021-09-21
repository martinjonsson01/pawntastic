package com.thebois.models.beings;

import java.util.Random;

import com.thebois.models.Position;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    /* Temporary hard-coded world size. Should be removed when pathfinding is implemented. */
    private static final int WORLD_SIZE = 50;
    private final Random random = new Random();

    /**
     * Instantiates with an initial position and a destination to travel to.
     *
     * @param currentPosition initial position.
     * @param destination     initial destination to travel to.
     */
    public Pawn(final Position currentPosition, final Position destination) {
        super(currentPosition, destination);
    }

    @Override
    public void update() {
        super.update();

        if (getPosition().equals(getDestination())) setRandomDestination();
    }

    private void setRandomDestination() {
        final Position randomPosition = new Position(random.nextInt(WORLD_SIZE),
                                                     random.nextInt(WORLD_SIZE));
        setDestination(randomPosition);
    }

    @Override
    public IBeing deepClone() {
        return new Pawn(this.getPosition(), this.getDestination());
    }

}
