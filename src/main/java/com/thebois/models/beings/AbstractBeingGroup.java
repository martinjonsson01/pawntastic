package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An abstract implementation of IBeingGroup.
 */
public abstract class AbstractBeingGroup implements IBeingGroup {

    private Collection<IBeing> beings = new ArrayList<>();

    /**
     * Adds a being to the internal collection of beings.
     *
     * @param being IBeing to be added.
     */
    protected void addBeing(final IBeing being) {
        beings.add(being);
    }

    @Override
    public void update() {
        for (final IBeing being : this.beings) {
            being.update();
        }
    }

    @Override
    public Collection<IBeing> getBeings() {
        final Collection<IBeing> clonedBeings = new ArrayList<>(beings.size());
        clonedBeings.addAll(beings);
        return clonedBeings;
    }

    protected void setBeings(final Collection<IBeing> beings) {
        this.beings = beings;
    }

    protected Collection<IBeing> getBeingsByReference() {
        return beings;
    }

}
