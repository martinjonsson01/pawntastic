package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.IStorable;

/**
 * Action give item from performer to receiver.
 */
public class GiveItemAction extends AbstractTimeAction implements Serializable {

    private static final float TIME_REQUIRED_TO_GIVE_ITEM = 1f;
    private static final float MINIMUM_GIVE_DISTANCE = 2f;
    private final IStorable storable;
    private final Position storablePosition;

    /**
     * Instantiate with a receiver to give item to.
     *
     * @param storable         Where the items should be stored.
     * @param storablePosition Where the storable is in the world.
     */
    public GiveItemAction(
        final IStorable storable, final Position storablePosition) {
        super(TIME_REQUIRED_TO_GIVE_ITEM);
        this.storable = storable;
        this.storablePosition = storablePosition;
    }

    @Override
    protected void onPerformCompleted(final IActionPerformer performer) {
        storable.tryAdd(performer.takeNextItem());
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        final boolean isCloseEnough =
            performer.getPosition().distanceTo(storablePosition) < MINIMUM_GIVE_DISTANCE;
        final boolean inventoryIsNotEmpty = !performer.isEmpty();

        return isCloseEnough && inventoryIsNotEmpty;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final GiveItemAction that = (GiveItemAction) other;
        return Objects.equals(storable, that.storable)
               && Objects.equals(
            storablePosition,
            that.storablePosition)
               && getTimeSpentPerforming() == that.getTimeSpentPerforming();
    }

    @Override
    public int hashCode() {
        return Objects.hash(storable, storablePosition);
    }

}
