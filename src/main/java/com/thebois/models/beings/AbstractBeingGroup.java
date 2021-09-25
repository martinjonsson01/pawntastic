package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;

import com.thebois.models.world.structures.IStructure;

/**
 * An abstract implementation of IBeingGroup.
 */
public abstract class AbstractBeingGroup implements IBeingGroup {

    private Collection<IBeing> beings = new ArrayList<>();
    private Collection<IStructure> knownStructures;

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
            being.updateKnownStructures(this.knownStructures);
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

    @Override
    public void updateKnownStructures(final Collection<IStructure> structures) {
        this.knownStructures = structures;
    }

}
