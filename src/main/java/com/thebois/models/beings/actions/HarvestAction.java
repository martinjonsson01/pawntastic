package com.thebois.models.beings.actions;

import java.util.Objects;

import com.thebois.models.beings.ITaskPerformer;
import com.thebois.models.world.resources.IResource;

/**
 * Used to perform the harvesting of a given resource.
 */
public class HarvestAction implements IAction {

    private final IResource resource;
    private boolean harvested = false;

    /**
     * Instantiates with a resource to harvest.
     *
     * @param resource What to harvest.
     */
    public HarvestAction(final IResource resource) {
        this.resource = resource;
    }

    @Override
    public void perform(final ITaskPerformer performer) {
        performer.addItem(resource.harvest());
        harvested = true;
    }

    @Override
    public boolean isCompleted(final ITaskPerformer performer) {
        return harvested;
    }

    @Override
    public boolean canPerform(final ITaskPerformer performer) {
        return resource.getPosition().distanceTo(performer.getPosition()) <= 1.0f;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, harvested);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof HarvestAction)) return false;
        final HarvestAction that = (HarvestAction) o;
        return harvested == that.harvested && resource.equals(that.resource);
    }

}
