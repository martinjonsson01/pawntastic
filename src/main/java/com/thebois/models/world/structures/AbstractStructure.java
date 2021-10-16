package com.thebois.models.world.structures;

import java.util.Collection;
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

    private Position position;
    private StructureType structureType;
    private Collection<ItemType> allNeededItems;
    private IInventory deliveredItems = new Inventory();

    /**
     * Creates a structure with a position and structure type.
     *
     * @param posX          Position in X-axis.
     * @param posY          Position in Y-axis.
     * @param structureType The type of structure to create.
     * @param allNeededItems The items needed to finalize construction.
     */
    AbstractStructure(
        final int posX,
        final int posY,
        final StructureType structureType,
        final Collection<ItemType> allNeededItems) {
        this.position = new Position(posX, posY);
        this.structureType = structureType;
        this.allNeededItems = allNeededItems;
    }

    /**
     * Creates a structure with a position and type.
     *
     * @param position      The position the structure have.
     * @param structureType The type of structure to create.
     * @param allNeededItems The items needed to finalize construction.
     */
    AbstractStructure(
        final Position position,
        final StructureType structureType,
        final Collection<ItemType> allNeededItems) {
        this((int) position.getPosX(), (int) position.getPosY(), structureType, allNeededItems);
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
        return deliveredItems.calculateDifference(allNeededItems);
    }

    @Override
    public boolean tryDeliverItem(final IItem deliveredItem) {
        final Collection<ItemType> neededItems = getNeededItems();
        if (neededItems.contains(deliveredItem.getType())) {
            deliveredItems.add(deliveredItem);
            return true;
        }
        return false;
    }

    @Override
    public float getBuiltRatio() {
        final float totalDelivered = this.allNeededItems.size() - getNeededItems().size();
        final float totalNeeded = this.allNeededItems.size();

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

}
