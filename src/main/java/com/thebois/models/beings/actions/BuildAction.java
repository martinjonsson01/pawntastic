package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
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
    private static final float ITEM_TRANSFER_TIME = 0.5f;
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
        final Collection<ItemType> neededItemTypes = toBuild.getNeededItems();
        final Optional<ItemType> neededItemType = neededItemTypes.stream().findFirst();
        if (neededItemType.isPresent()) {
            final IItem item = ItemFactory.fromType(neededItemType.get());
            toBuild.tryDeliverItem(item);
        }
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        // Note: when items have to be fetched from stockpiles (instead of magically created),
        // this will have to take into account whether the performer has the required items
        // in its inventory.
        return performer.getPosition().distanceTo(toBuild.getPosition()) < MINIMUM_BUILD_DISTANCE;
    }

}
