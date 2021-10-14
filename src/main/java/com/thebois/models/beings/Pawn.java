package com.thebois.models.beings;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.items.Log;
import com.thebois.models.inventory.items.Rock;
import com.thebois.models.world.structures.IStructure;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    /* Temporary hard-coded world size. Should be removed when pathfinding is implemented. */
    private static final int WORLD_SIZE = 50;
    private final Random random;
    private IStructure closestStructure;

    /**
     * Instantiates with an initial position and a destination to travel to.
     *
     * @param startPosition Initial position.
     * @param destination   Initial destination to travel to.
     * @param random        The generator of random numbers.
     * @param pathFinder    The generator of paths to positions in the world.
     * @param finder        Used to find things in the world.
     */
    public Pawn(
        final Position startPosition,
        final Position destination,
        final Random random,
        final IPathFinder pathFinder,
        final IFinder finder) {
        super(startPosition, destination, pathFinder, finder);
        this.random = random;
    }

    @Override
    public void update() {
        super.update();

        if (closestStructure == null || closestStructure.isCompleted()) {
            closestStructure = findClosestStructure();
        }

        if (closestStructure != null) {
            setFinalDestination(closestStructure.getPosition());
            deliverItemToStructure(closestStructure);
        }

        if (getDestination().isEmpty()) setRandomDestination();
    }

    private void setRandomDestination() {
        final int randomX = random.nextInt(WORLD_SIZE);
        final int randomY = random.nextInt(WORLD_SIZE);
        final Position destination = new Position(randomX, randomY);

        final Collection<Position> newPath = getPathFinder().path(getPosition(), destination);
        setPath(newPath);
    }

    protected IStructure findClosestStructure() {
        final Optional<IStructure> structure =
            getFinder().findNearestIncompleteStructure(this.getPosition());
        return structure.orElse(null);
    }

    private void setFinalDestination(final Position destination) {
        final Position nextTo = nearestNeighborOf(destination);
        // If finalDestination is empty or is not set to the given destination
        if (getFinalDestination() == null || !getFinalDestination().equals(nextTo)) {
            final Collection<Position> newPath = getPathFinder().path(this.getPosition(), nextTo);
            setPath(newPath);
        }
    }

    protected void deliverItemToStructure(final IStructure structure) {
        if (structure.getPosition().distanceTo(this.getPosition()) < 2f) {
            structure.deliverItem(new Rock());
            structure.deliverItem(new Log());
        }
    }

}
