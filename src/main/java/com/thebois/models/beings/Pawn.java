package com.thebois.models.beings;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.world.inventory.IItem;
import com.thebois.models.world.structures.IStructure;

/**
 * Pawn.
 */
public class Pawn extends AbstractBeing {

    /* Temporary hard-coded world size. Should be removed when pathfinding is implemented. */
    private static final int WORLD_SIZE = 50;
    private final Random random;

    /**
     * Instantiates with an initial position and a destination to travel to.
     *
     * @param currentPosition initial position.
     * @param destination     initial destination to travel to.
     * @param random          the generator of random numbers.
     * @param finder          Finder used to locate things in the world
     */
    public Pawn(final Position currentPosition,
                final Position destination,
                final Random random,
                final IFinder finder) {
        super(currentPosition, destination);
        this.random = random;
        setFinder(finder);
    }

    @Override
    public void update() {
        super.update();
        deliverItemToNearestStructure();
        pickNearestIncompleteStructureAsDestination();
        if (getPosition().equals(getDestination())) setRandomDestination();
    }

    private void setRandomDestination() {
        final Position randomPosition = new Position(random.nextInt(WORLD_SIZE),
                                                     random.nextInt(WORLD_SIZE));
        setDestination(randomPosition);
    }

    protected void pickNearestIncompleteStructureAsDestination() {
        final Collection<Optional<IStructure>> structures =
            getFinder().findNearestStructure(this.getPosition(), 10);
        final Boolean[] found = {false};
        for (final Optional<IStructure> structure : structures) {
            structure.ifPresent(iStructure -> {
                if (structure.get().builtStatus() < 1f) {
                    setDestination(iStructure.getPosition());
                    found[0] = true;
                }
            });
            if (found[0]) return;
        }
    }

    protected void deliverItemToNearestStructure() {
        final Optional<IStructure> structure = getFinder().findNearestStructure(this.getPosition());
        structure.ifPresent(iStructure -> {
            if (iStructure.getPosition().distanceTo(getPosition()) < 2f) {
                iStructure.deliverItem(new IItem() {
                });
            }
        });
    }

}
