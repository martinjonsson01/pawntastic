package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An abstract implementation of IBeingGroup.
 */
public abstract class AbstractBeingGroup implements IBeingGroup {

    private final Collection<IBeing> beings = new ConcurrentLinkedQueue<>();

    /**
     * Adds a being to the internal collection of beings.
     *
     * @param being IBeing to be added.
     */
    public void addBeing(final IBeing being) {
        beings.add(being);
    }

    @Override
    public void update(final float deltaTime) {
        for (final IBeing being : this.beings) {
            being.update(deltaTime);
        }
    }

    @Override
    public Collection<IBeing> getBeings() {
        final Collection<IBeing> clonedBeings = new ArrayList<>(beings.size());
        clonedBeings.addAll(beings);
        return clonedBeings;
    }
}
