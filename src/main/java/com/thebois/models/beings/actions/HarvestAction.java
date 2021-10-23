package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Objects;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.world.resources.IResource;

/**
 * Used to perform the harvesting of a given resource.
 */
public class HarvestAction extends AbstractTimeAction implements Serializable {

    /**
     * The distance between the performer and the storable, in tiles.
     */
    private static final float MINIMUM_HARVEST_DISTANCE = 1f;
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

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        final boolean isCloseEnough =
            resource.getPosition().distanceTo(performer.getPosition()) <= MINIMUM_HARVEST_DISTANCE;
        final boolean inventoryHasSpace = performer.canFitItem(resource.getType().getItemType());
        return isCloseEnough && inventoryHasSpace;
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
