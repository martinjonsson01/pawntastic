package com.thebois.models.world.structures;

import com.thebois.models.Position;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.inventory.items.Log;
import com.thebois.models.inventory.items.Rock;

/**
 * A structure of type House.
 */
public class House extends AbstractStructure {

    /**
     * Creates a house structure at a given position in the world.
     *
     * @param posX Position in X-axis.
     * @param posY Position in Y-axis.
     */
    public House(final int posX, final int posY) {
        this(new Position(posX, posY));
    }

    /**
     * Creates a house structure at a given position in the world.
     *
     * @param position The position of the house.
     */
    public House(final Position position) {
        super(position, StructureType.HOUSE, generateNeededItemsInventory());
    }

    private static IInventory generateNeededItemsInventory() {
        final IInventory neededItems = new Inventory();

        final int numberOfItems = 10;
        for (int i = 0; i < numberOfItems; i++) {
            neededItems.add(new Log());
            neededItems.add(new Rock());
        }

        return neededItems;
    }

    @Override
    public House deepClone() {
        return new House(getPosition());
    }

    @Override
    public float getCost() {
        return Float.MAX_VALUE;
    }

}
