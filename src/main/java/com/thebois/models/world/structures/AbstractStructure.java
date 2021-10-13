package com.thebois.models.world.structures;

import java.util.Collection;
import java.util.Objects;

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
    private IInventory allNeededItems = new Inventory();
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
        int posX,
        int posY,
        StructureType structureType,
        final IInventory allNeededItems) {
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
        Position position,
        StructureType structureType,
        final IInventory allNeededItems) {
        this((int) position.getPosX(), (int) position.getPosY(), structureType, allNeededItems);
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AbstractStructure that = (AbstractStructure) o;
        return position.equals(that.position) && structureType == that.structureType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, structureType);
    }

    @Override
    public StructureType getType() {
        return structureType;
    }

    @Override
    public Collection<ItemType> neededItems() {
        return allNeededItems.calculateDifference(deliveredItems);
    }

    @Override
    public boolean deliverItem(final IItem deliveredItem) {
        if (neededItems().contains(deliveredItem.getType())) {
            deliveredItems.add(deliveredItem);
            return true;
        }
        return false;
    }

    @Override
    public float builtStatus() {
        if (allNeededItems.isEmpty()) {
            return 1f;
        }
        else {
            final float totalDelivered =
                this.deliveredItems.calculateDifference(this.allNeededItems).size();
            final float totalNeeded = this.allNeededItems.size();

            return totalDelivered / totalNeeded;
        }
    }

    @Override
    public boolean isCompleted() {
        return Float.compare(builtStatus(), 1f) >= 0;
    }

    @Override
    public IItem dismantle(final ItemType retrieving) {
        if (neededItems().contains(retrieving)) {
            return deliveredItems.take(retrieving);
        }
        return null;
    }

}
