package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.IStoreable;

/**
 * Action give item from performer to receiver.
 */
public class GiveItemAction implements IAction, Serializable {

    private static final float MINIMUM_GIVE_DISTANCE = 2f;
    private final IStoreable storeable;
    private final Position storablePosition;

    /**
     * Instantiate with a receiver to give item to.
     *
     * @param storeable        Where the items should be stored.
     * @param storablePosition Where the storable is in the world.
     */
    public GiveItemAction(
        final IStoreable storeable, final Position storablePosition) {
        this.storeable = storeable;

        this.storablePosition = storablePosition;
    }

    @Override
    public void perform(final IActionPerformer performer) {
        if (!performer.isEmpty()) {
            storeable.tryAdd(performer.takeNextItem());
        }
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return performer.isEmpty();
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return performer.getPosition().distanceTo(storablePosition) < MINIMUM_GIVE_DISTANCE;
    }

}
