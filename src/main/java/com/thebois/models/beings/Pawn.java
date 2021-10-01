package com.thebois.models.beings;

import com.thebois.models.Position;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    //private final Random random;

    /**
     * Instantiates with an initial position and a destination to travel to.
     *
     * @param startPosition The initial position.
     */
    public Pawn(final Position startPosition) {
        super(startPosition);
    }

    @Override
    public void update() {
        super.update();

        //if (getDestination().isEmpty()) setRandomDestination();
    }

    /*private void setRandomDestination() {
        final int randomX = random.nextInt(WORLD_SIZE);
        final int randomY = random.nextInt(WORLD_SIZE);
        final Position destination = new Position(randomX, randomY);

        final Collection<Position> newPath = getPathFinder().path(getPosition(), destination);
        setPath(newPath);
    }*/
}
