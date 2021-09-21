package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An abstract implementation of IBeingGroup.
 */
public abstract class AbstractBeingGroup implements IBeingGroup {

    private final Collection<IBeing> beings = new ArrayList<>();

    /**
     * Creates a BeingGroup containing 5 Beings.
     */
    public AbstractBeingGroup() {
        final int numberOfBeings = 5;
        createBeing(numberOfBeings);
    }

    /**
     * Creates an AbstractBeingGroup containing n IBeings.
     *
     * @param numberOfBeings the number of IBeings to create.
     */
    public AbstractBeingGroup(int numberOfBeings) {
        createBeing(numberOfBeings);
    }

    @Override
    public Collection<IBeing> getBeings() {
        return beings;
    }

    @Override
    public void update() {
        for (IBeing being : this.beings) {
            being.update();
        }
    }

    /**
     * Adds a being to the internal collection of beings.
     *
     * @param being IBeing to be added.
     */
    protected void addBeing(IBeing being) {
        beings.add(being);
    }

    private void createBeing(int numberOfBeings) {
        for (int i = 0; i < numberOfBeings; i++) {
            addBeing(new Pawn());
        }
    }

}
