package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.SpawnPawnsEvent;
import com.thebois.models.Position;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

/**
 * Base structure for the game.
 */
abstract class AbstractStructure implements IStructure {

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
    public StructureType getType() {
        return structureType;
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

    private int countAllNeededItems() {
        return allNeededItems.values().stream().reduce(0, Integer::sum);
    }

    @Override
    public boolean tryDeliverItem(final IItem deliveredItem) {
        final Collection<ItemType> neededItems = getNeededItems();
        boolean successfulDelivery = false;
        if (neededItems.contains(deliveredItem.getType())) {
            successfulDelivery = deliveredItems.tryAdd(deliveredItem);
        }
        if (isCompleted()) {
            postSpawnPawnsEvent(getPosition());
        }
        return successfulDelivery;
    }

    @Override
    public float getBuiltRatio() {
        final float totalNeeded = countAllNeededItems();
        final float totalDelivered = totalNeeded - getNeededItems().size();

        return totalDelivered / totalNeeded;
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

    private void postSpawnPawnsEvent(final Position spawnPosition) {
        final SpawnPawnsEvent spawnPawnsEvent = new SpawnPawnsEvent(2, spawnPosition, 2f);
        Pawntastic.getEventBus().post(spawnPawnsEvent);
    }

}
