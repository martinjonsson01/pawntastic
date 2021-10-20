package com.thebois.models.beings.actions;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.thebois.models.beings.IActionPerformer;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.structures.IStructure;

/**
 * Used to build structures.
 */
public class BuildAction implements IAction, Serializable {

    /**
     * The minimum distance at which a being has to be from a structure to be able to build it, in
     * tiles.
     */
    private static final float MINIMUM_BUILD_DISTANCE = 2f;
    private final IStructure toBuild;

    /**
     * Instantiates with a structure to build.
     *
     * @param toBuild What to build.
     */
    public BuildAction(final IStructure toBuild) {
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
    public void perform(final IActionPerformer performer, final float deltaTime) {
        final Collection<ItemType> neededItemTypes = toBuild.getNeededItems();
        final List<IItem> neededItems = neededItemTypes
            .stream()
            .map(ItemFactory::fromType)
            .collect(Collectors.toList());
        neededItems.forEach(toBuild::tryDeliverItem);
    }

    @Override
    public boolean isCompleted(final IActionPerformer performer) {
        return toBuild.isCompleted();
    }

    @Override
    public boolean canPerform(final IActionPerformer performer) {
        // Note: when items have to be fetched from stockpiles (instead of magically created),
        // this will have to take into account whether the performer has the required items
        // in its inventory.
        return performer
                   .getPosition()
                   .distanceTo(toBuild.getPosition()) < MINIMUM_BUILD_DISTANCE;
    }

}
