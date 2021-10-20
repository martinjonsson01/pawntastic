package com.thebois.models.beings.actions;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.beings.IGiver;
import com.thebois.models.inventory.items.ItemType;

/**
 * Action take takes an item from a giver.
 */
public class TakeItemAction implements IAction {

    private static final float MINIMUM_TAKE_DISTANCE = 2f;
    private final IGiver giver;
    private final ItemType itemType;

    /**
     * Instantiate with a giver to take item from.
     *
     * @param giver    The giver to take items from.
     * @param itemType The type of item to take.
     */
    public TakeItemAction(final IGiver giver, final ItemType itemType) {
        this.giver = giver;
        this.itemType = itemType;
    }

    @Override
    public void perform(final IActionPerformer performer) {
        performer.tryGiveItem(giver.takeItem(itemType));
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return !giver.hasItem(itemType);
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return performer.getPosition().distanceTo(giver.getPosition()) < MINIMUM_TAKE_DISTANCE;
    }

}
