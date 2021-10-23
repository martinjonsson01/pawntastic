package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.IStorable;
import com.thebois.models.inventory.items.ItemType;

/**
 * Action give item from performer to a storable.
 *
 * @author Mathias
 */
public class GiveItemAction extends AbstractTimeAction implements Serializable {

    /**
     * The number of seconds required to give the item.
     */
    private static final float TIME_REQUIRED_TO_GIVE_ITEM = 1f;
    /**
     * The distance between the performer and the storable, in tiles.
     */
    private static final float MINIMUM_GIVE_DISTANCE = 2f;
    private final IStorable storable;
    private final Position storablePosition;
    private final ItemType itemType;

    /**
     * Instantiate with a storable to give item to.
     *
     * @param storable         Where the items should be stored.
     * @param itemType         The type of item to remove.
     * @param storablePosition Where the storable is in the world.
     */
    public GiveItemAction(
        final IStorable storable, final ItemType itemType, final Position storablePosition) {
        super(TIME_REQUIRED_TO_GIVE_ITEM);
        this.storable = storable;
        this.storablePosition = storablePosition;
        this.itemType = itemType;
    }

    @Override
    protected void onPerformCompleted(final IActionPerformer performer) {
        storable.tryAdd(performer.take(itemType));
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        final boolean isCloseEnough =
            performer.getPosition().distanceTo(storablePosition) < MINIMUM_GIVE_DISTANCE;
        final boolean performerHasItem = performer.hasItem(itemType);

        return isCloseEnough && performerHasItem;
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
               && Objects.equals(itemType, that.itemType)
               && getTimeSpentPerforming() == that.getTimeSpentPerforming();
    }

    @Override
    public int hashCode() {
        return Objects.hash(storable, storablePosition);
    }

}
