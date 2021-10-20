package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.IReceiver;
import com.thebois.models.inventory.items.ItemType;

/**
 * Action give item from performer to receiver.
 */
public class GiveItemAction implements IAction, Serializable {

    private static final float MINIMUM_GIVE_DISTANCE = 2f;
    private final IReceiver receiver;
    private final ItemType itemType;

    /**
     * Instantiate with a receiver to give item to.
     *
     * @param receiver The receiver.
     * @param itemType The type of item to give.
     */
    public GiveItemAction(
        final IReceiver receiver, final ItemType itemType) {
        this.receiver = receiver;
        this.itemType = itemType;
    }

    @Override
    public void perform(final IActionPerformer performer) {
        if (performer.hasItem(itemType)) {
            receiver.tryGiveItem(performer.takeItem(itemType));
        }
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return !performer.hasItem(itemType);
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return performer.getPosition().distanceTo(receiver.getPosition()) < MINIMUM_GIVE_DISTANCE;
    }

}
