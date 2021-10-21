package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.IStoreable;
import com.thebois.models.inventory.items.ItemType;

/**
 * Action give item from performer to receiver.
 */
public class GiveItemAction implements IAction, Serializable {

    private static final float MINIMUM_GIVE_DISTANCE = 2f;
    private final IStoreable storeable;
    private final ItemType itemType;
    private final Position storablePosition;

    /**
     * Instantiate with a receiver to give item to.
     *
     * @param storeable        Where the items should be stored.
     * @param itemType         The type of item to give.
     * @param storablePosition Where the storable is in the world.
     */
    public GiveItemAction(
        final IStoreable storeable, final ItemType itemType, final Position storablePosition) {
        this.storeable = storeable;
        this.itemType = itemType;

        this.storablePosition = storablePosition;
    }

    @Override
    public void perform(final IActionPerformer performer) {
        if (performer.hasItem(itemType)) {
            storeable.tryAdd(performer.take(itemType));
        }
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return !performer.hasItem(itemType);
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return performer.getPosition().distanceTo(storablePosition) < MINIMUM_GIVE_DISTANCE;
    }

}
