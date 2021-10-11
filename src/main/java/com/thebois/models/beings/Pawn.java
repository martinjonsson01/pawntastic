package com.thebois.models.beings;

import java.util.Collection;
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

        updateClosestStructure();
        if (closestStructure != null) {
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

    protected void updateClosestStructure() {
        if (closestStructure != null && getFinalDestination().isPresent()) {
            // If the structure is completed or destination is unreachable pick a new one
            if (closestStructure.isCompleted() || closestStructure.getPosition().equals(
                getFinalDestination().get())) {
                closestStructure = findIncompleteStructure();
                setClosestStructureAsFinalDestination();
            }
        }
        else {
            closestStructure = findIncompleteStructure();
            setClosestStructureAsFinalDestination();
        }
    }

    protected IStructure findIncompleteStructure() {
        final Collection<IStructure> structures =
            getFinder().findNearestStructures(this.getPosition(), 10);

        for (final IStructure structure : structures) {
            if (!structure.isCompleted()) {
                return structure;
            }
        }
        return null;
    }

    private void setClosestStructureAsFinalDestination() {
        if (closestStructure != null) {
            setFinalDestination(closestStructure.getPosition());
        }
    }

    private void setFinalDestination(final Position destination) {
        final Position nextTo = nearestNeighborOf(destination);
        if (getFinalDestination().isPresent()) {
            if (!getFinalDestination().get().equals(nextTo)) {
                final Collection<Position> newPath = getPathFinder().path(getPosition(), nextTo);
                setPath(newPath);
            }
        }
        else {
            final Collection<Position> newPath = getPathFinder().path(getPosition(), nextTo);
            setPath(newPath);
        }
    }

    protected void deliverItemToStructure(final IStructure structure) {
        if (structure.getPosition().distanceTo(getPosition()) < 2f) {
            structure.deliverItem(new Rock());
            structure.deliverItem(new Log());
        }
    }

}
