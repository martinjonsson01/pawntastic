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
    private Optional<IStructure> closestStructure = Optional.empty();
    private Optional<IStructure> previousClosestStructure = Optional.empty();

    /**
     * Instantiates with an initial position and a destination to travel to.
     *
     * @param startPosition initial position.
     * @param destination   initial destination to travel to.
     * @param random        the generator of random numbers.
     * @param pathFinder    The generator of paths to positions in the world.
     * @param finder          Finder used to locate things in the world
     */
    public Pawn(
        final Position startPosition,
        final Position destination,
        final Random random,
        final IPathFinder pathFinder,
        final IFinder finder) {
        super(startPosition, destination, pathFinder);
        this.random = random;
        setFinder(finder);
    }

    @Override
    public void update() {
        super.update();
        deliverItemToNearestStructure();
        updateClosestStructure();

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
        if (closestStructure.isPresent()) {
            if (closestStructure.get().isCompleted()) {
                closestStructure = findIncompleteStructure();
                setClosestStructureAsFinalDestination();
            }
        }
        else {
            closestStructure = findIncompleteStructure();
            setClosestStructureAsFinalDestination();
        }
    }

    protected Optional<IStructure> findIncompleteStructure() {
        final Collection<Optional<IStructure>> structures =
            getFinder().findNearestStructures(this.getPosition(), 10);

        for (final Optional<IStructure> structure : structures) {
            if (structure.isPresent()) {
                if (!structure.get().isCompleted()) {
                    return structure;
                }
            }
            else {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private void setClosestStructureAsFinalDestination() {
        closestStructure.ifPresent(structure -> setFinalDestination(structure.getPosition()));
    }

    private void setFinalDestination(final Position destination) {
        final Position nextTo = nearestPositionNextTo(destination);
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

    protected void deliverItemToNearestStructure() {
        final Optional<IStructure> structure = getFinder().findNearestStructure(this.getPosition());
        structure.ifPresent(iStructure -> {
            if (iStructure.getPosition().distanceTo(getPosition()) < 2f) {
                iStructure.deliverItem(new Rock() {
                });
                iStructure.deliverItem(new Log() {
                });
            }
        });
    }

}
