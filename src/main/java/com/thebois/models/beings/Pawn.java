package com.thebois.models.beings;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    /* Temporary hard-coded world size. Should be removed when pathfinding is implemented. */
    private static final int WORLD_SIZE = 50;
    private final Random random;
    private IStructure closestIncompleteStructure;

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
        final IStructureFinder finder) {
        super(startPosition, destination, pathFinder, finder);
        this.random = random;
    }

    @Override
    public void update() {
        super.update();

        if (closestIncompleteStructure == null || closestIncompleteStructure.isCompleted()) {
            closestIncompleteStructure = findNearestIncompleteStructure();
        }

        if (closestIncompleteStructure != null) {
            setFinalDestination(closestIncompleteStructure.getPosition());
            tryDeliverItemToStructure(closestIncompleteStructure);
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

    protected IStructure findNearestIncompleteStructure() {
        final Optional<IStructure> structure =
            getStructureFinder().findNearestIncompleteStructure(
                this.getPosition(), StructureType.HOUSE);
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

    protected void tryDeliverItemToStructure(final IStructure structure) {
        if (structure.getPosition().distanceTo(this.getPosition()) < 2f) {
            structure.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
            structure.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
        }
    }

}
