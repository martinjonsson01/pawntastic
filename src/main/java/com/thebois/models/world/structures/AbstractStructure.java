package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.thebois.models.Position;
import com.thebois.models.world.inventory.IItem;

/**
 * Base structure for the game.
 */
abstract class AbstractStructure implements IStructure {

    private Position position;
    private StructureType structureType;
    private Collection<IItem> collectionOfAllNeededItems;
    private Collection<IItem> collectionOfDeliveredItems;

    /**
     * Creates a structure with a position and structure type.
     *
     * @param posX          Position in X-axis
     * @param posY          Position in Y-axis
     * @param structureType The type of structure to create.
     */
    AbstractStructure(int posX, int posY, StructureType structureType) {
        this.position = new Position(posX, posY);
        this.structureType = structureType;
    }

    /**
     * Creates a structure with a position and type.
     *
     * @param position      The position the structure have.
     * @param structureType The type of structure to create.
     */
    AbstractStructure(Position position, StructureType structureType) {
        this((int) position.getPosX(), (int) position.getPosY(), structureType);
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
    public Collection<IItem> neededItems() {
        // Make a copy of all IItems in collectionOfAllNeededItems
        final Collection<IItem> itemDifference = new ArrayList<>(collectionOfAllNeededItems);
        // Remove IItems that have been delivered
        collectionOfDeliveredItems.forEach(itemDifference::remove);
        // Return difference of the two collections
        return itemDifference;
    }

    @Override
    public boolean deliverItem(final IItem deliveredItem) {
        if (collectionOfAllNeededItems.contains(deliveredItem)) {
            collectionOfDeliveredItems.add(deliveredItem);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public float builtStatus() {
        final float totalDelivered = this.collectionOfDeliveredItems.size();
        final float totalNeeded = this.collectionOfAllNeededItems.size();
        return totalDelivered / totalNeeded;
    }

    @Override
    public boolean dismantle(final IItem retrieving) {
        if (collectionOfDeliveredItems.contains(retrieving)) {
            collectionOfDeliveredItems.remove(retrieving);
            return true;
        }
        else {
            return false;
        }
    }

    protected void setAllNeededItems(final Collection<IItem> allNeededItems) {
        this.collectionOfAllNeededItems = allNeededItems;
    }

}
