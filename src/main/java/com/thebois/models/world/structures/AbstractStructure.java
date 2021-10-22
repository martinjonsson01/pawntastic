package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.thebois.models.Position;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Base structure for the game.
 */
abstract class AbstractStructure implements IStructure {

    /**
     * The cost of walking on the structure when it is not yet built.
     */
    private static final float BLUEPRINT_WALK_COST = 0f;
    private final Position position;
    private final StructureType structureType;
    private final Map<ItemType, Integer> allNeededItems;
    private final IInventory deliveredItems = new Inventory();

    /**
     * Creates a structure with a position and type.
     *
     * @param position       The position the structure have.
     * @param structureType  The type of structure to create.
     * @param allNeededItems The items needed to finalize construction.
     */
    AbstractStructure(
        final Position position,
        final StructureType structureType,
        final Map<ItemType, Integer> allNeededItems) {
        this.position = position;
        this.structureType = structureType;
        this.allNeededItems = allNeededItems;
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

    @Override
    public float getCost() {
        if (getBuiltRatio() != 1f) return BLUEPRINT_WALK_COST;
        return getCostWhenBuilt();
    }

    protected abstract float getCostWhenBuilt();

    private int countAllNeededItems() {
        return allNeededItems.values().stream().reduce(0, Integer::sum);
    }

    @Override
    public Collection<ItemType> getNeededItems() {
        final Collection<ItemType> neededItems = new ArrayList<>();
        for (final ItemType itemType : allNeededItems.keySet()) {
            final int numberOfNeededItems = allNeededItems.get(itemType) - deliveredItems.numberOf(
                itemType);

            for (int i = 0; i < numberOfNeededItems; i++) {
                neededItems.add(itemType);
            }
        }
        return neededItems;
    }

    @Override
    public boolean tryDeliverItem(final IItem deliveredItem) {
        final Collection<ItemType> neededItems = getNeededItems();
        if (neededItems.contains(deliveredItem.getType())) {
            return deliveredItems.tryAdd(deliveredItem);
        }
        return false;
    }

    @Override
    public float getBuiltRatio() {
        final int totalNeeded = countAllNeededItems();
        final int totalDelivered = totalNeeded - getNeededItems().size();

        return (float) totalDelivered / totalNeeded;
    }

    @Override
    public boolean isCompleted() {
        return Float.compare(getBuiltRatio(), 1f) >= 0;
    }

    @Override
    public Optional<IItem> tryDismantle(final ItemType retrieving) {
        if (deliveredItems.hasItem(retrieving)) {
            return Optional.of(deliveredItems.take(retrieving));
        }
        return Optional.empty();
    }

    @Override
    public StructureType getType() {
        return structureType;
    }

}
