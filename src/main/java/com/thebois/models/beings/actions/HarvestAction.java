package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Objects;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.world.resources.IResource;

/**
 * Used to perform the harvesting of a given resource.
 */
public class HarvestAction extends AbstractTimeAction implements Serializable {

    private final IResource resource;

    /**
     * Instantiates with a resource to harvest.
     *
     * @param resource What to harvest.
     */
    public HarvestAction(final IResource resource) {
        super(resource.getHarvestTime());
        this.resource = resource;
    }

    @Override
    protected void onPerformCompleted(final IActionPerformer performer) {
        performer.tryAdd(resource.harvest());
    }

    /*
    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        final ItemType itemTypeOfResource = resource.getType().getItemType();
        return !performer.canFitItem(itemTypeOfResource);
    }
    */

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return resource.getPosition().distanceTo(performer.getPosition()) <= 1.0f;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, getTimeSpentPerforming());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof HarvestAction)) return false;
        final HarvestAction that = (HarvestAction) other;
        return resource.equals(that.resource)
               && getTimeSpentPerforming() == that.getTimeSpentPerforming();
    }

}
