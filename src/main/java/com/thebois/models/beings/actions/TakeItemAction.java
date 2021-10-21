package com.thebois.models.beings.actions;

import java.io.Serializable;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.ITakeable;
import com.thebois.models.inventory.items.ItemType;

/**
 * Action take takes an item from a giver.
 */
public class TakeItemAction implements IAction, Serializable {

    private static final float MINIMUM_TAKE_DISTANCE = 2f;
    private final ITakeable takeable;
    private final ItemType itemType;
    private final Position takeablePosition;
    private int amountToTake;

    /**
     * Instantiate with takeable and type of item to take.
     *
     * @param takeable         From where the item should be taken from.
     * @param itemType         The type of item to take.
     * @param amount           The amount of items to take.
     * @param takeablePosition The position of the takeable.
     */
    public TakeItemAction(
        final ITakeable takeable,
        final ItemType itemType,
        final int amount,
        final Position takeablePosition) {
        this.itemType = itemType;
        this.takeable = takeable;
        amountToTake = amount;
        this.takeablePosition = takeablePosition;
    }

    @Override
    public void perform(final IActionPerformer performer) {
        if (takeable.hasItem(itemType)) {
            performer.tryAdd(takeable.take(itemType));
            amountToTake--;
        }
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        // isCompleted if takeable has no items left or if amount to take is 0.
        return !takeable.hasItem(itemType) || amountToTake == 0;
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        return performer.getPosition().distanceTo(takeablePosition) < MINIMUM_TAKE_DISTANCE;
    }

}
