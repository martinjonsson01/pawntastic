package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.StructureCompletedEvent;
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
        return position;
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
            for (int i = 0; i < getNumberOfNeededItem(itemType); i++) {
                neededItems.add(itemType);
            }
        }
        return neededItems;
    }

    @Override
    public int getNumberOfNeededItem(final ItemType itemType) {
        if (!allNeededItems.containsKey(itemType)) return 0;
        return allNeededItems.get(itemType) - deliveredItems.numberOf(itemType);
    }

    @Override
    public boolean tryDeliverItem(final IItem deliveredItem) {
        final Collection<ItemType> neededItems = getNeededItems();
        boolean successfulDelivery = false;
        if (neededItems.contains(deliveredItem.getType())) {
            successfulDelivery = deliveredItems.tryAdd(deliveredItem);
        }
        if (isCompleted()) {
            postStructureCompletedEvent();
        }
        return successfulDelivery;
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

    protected void postStructureCompletedEvent() {
        final StructureCompletedEvent structureCompletedEvent =
            new StructureCompletedEvent(structureType, getPosition());
        Pawntastic.getEventBus().post(structureCompletedEvent);
    }

    @Override
    public StructureType getType() {
        return structureType;
    }

}
