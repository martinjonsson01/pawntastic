package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.structures.IStructure;

/**
 * Used to build structures.
 */
public class BuildAction extends AbstractTimeAction implements Serializable {

    /**
     * The minimum distance at which a being has to be from a structure to be able to build it, in
     * tiles.
     */
    private static final float MINIMUM_BUILD_DISTANCE = 2f;
    /**
     * How many seconds it takes to transfer a single item to the structure.
     */
    private static final float ITEM_TRANSFER_TIME = 2f;
    private final IStructure toBuild;

    /**
     * Instantiates with a structure to build.
     *
     * @param toBuild What to build.
     */
    public BuildAction(final IStructure toBuild) {
        super(ITEM_TRANSFER_TIME);
        this.toBuild = toBuild;
    }

    @Override
    public int hashCode() {
        return Objects.hash(toBuild);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof BuildAction)) return false;
        final BuildAction that = (BuildAction) other;
        return toBuild.equals(that.toBuild);
    }

    @Override
    protected void onPerformCompleted(final IActionPerformer performer) {
        // Will never be null as the action can not be performed if canPerform doesn't pass
        final ItemType neededItem = getNeededItemFromPerformer(performer);
        toBuild.tryDeliverItem(performer.take(neededItem));
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        // If you change the line below to not handle null, it can break onPerformCompleted method
        final boolean performerHasNeededItem = getNeededItemFromPerformer(performer) != null;
        final boolean isCloseEnough =
            performer.getPosition().distanceTo(toBuild.getPosition()) < MINIMUM_BUILD_DISTANCE;
        return isCloseEnough && performerHasNeededItem;
    }

    private ItemType getNeededItemFromPerformer(final IActionPerformer performer) {
        final Collection<ItemType> neededItems = toBuild.getNeededItems();
        for (final ItemType neededItem : neededItems) {
            if (performer.hasItem(neededItem)) {
                return neededItem;
            }
        }
        return null;
    }

}
