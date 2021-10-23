package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.items.ItemType;

/**
 * Action take takes an item from a giver.
 */
public class TakeItemAction extends AbstractTimeAction implements Serializable {

    private static final float TIME_REQUIRED_TO_TAKE_ITEM = 1f;
    private static final float MINIMUM_TAKE_DISTANCE = 2f;
    private final ITakeable takeable;
    private final ItemType itemType;
    private final Position takeablePosition;

    /**
     * Instantiate with takeable and type of item to take.
     *
     * @param takeable         From where the item should be taken from.
     * @param itemType         The type of item to take.
     * @param takeablePosition The position of the takeable.
     */
    public TakeItemAction(
        final ITakeable takeable,
        final ItemType itemType,
        final Position takeablePosition) {
        super(TIME_REQUIRED_TO_TAKE_ITEM);
        this.itemType = itemType;
        this.takeable = takeable;
        this.takeablePosition = takeablePosition;
    }

    /*
        @Override
        public boolean isCompleted(final IActionPerformer performer) {
            // isCompleted if takeable has no items left or if amount to take is 0.
            return !takeable.hasItem(itemType) || amountToTake == 0;
        }
    */

    @Override
    protected void onPerformCompleted(final IActionPerformer performer) {
        if (takeable.hasItem(itemType) && performer.canFitItem(itemType)) {
            performer.tryAdd(takeable.take(itemType));
        }
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return performer.getPosition().distanceTo(takeablePosition) < MINIMUM_TAKE_DISTANCE;
    }

}
