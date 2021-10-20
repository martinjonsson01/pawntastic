package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Objects;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.world.resources.IResource;

/**
 * Used to perform the harvesting of a given resource.
 */
public class HarvestAction implements IAction, Serializable {

    private final IResource resource;

    /**
     * Instantiates with a resource to harvest.
     *
     * @param resource What to harvest.
     */
    public HarvestAction(final IResource resource) {
        this.resource = resource;
    }

    @Override
    public void perform(final IActionPerformer performer) {
        performer.addItem(resource.harvest());
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return !performer.canFitItem(resource.getType().getItemType());
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return resource.getPosition().distanceTo(performer.getPosition()) <= 1.0f;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof HarvestAction)) return false;
        final HarvestAction that = (HarvestAction) o;
        return resource.equals(that.resource);
    }

}
