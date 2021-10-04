package com.thebois.models.world.structures;

import java.util.ArrayList;
import java.util.Collection;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
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
        super(position, StructureType.HOUSE);
        final Collection<IItem> neededItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            neededItems.add(new Log());
            neededItems.add(new Rock());
        }
        setAllNeededItems(neededItems);
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
