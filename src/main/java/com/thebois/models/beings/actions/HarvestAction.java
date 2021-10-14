package com.thebois.models.beings.actions;

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

}
