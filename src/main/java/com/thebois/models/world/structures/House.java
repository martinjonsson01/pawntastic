package com.thebois.models.world.structures;

import java.util.EnumMap;
import java.util.Map;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.ItemType;

/**
 * A structure of type House.
 */
class House extends AbstractStructure {

    /**
     * Creates a house structure at a given position in the world.
     *
     * @param position The position of the house.
     */
    House(final Position position) {
        super(position, StructureType.HOUSE, getBuildMaterials());
    }

    private static Map<ItemType, Integer> getBuildMaterials() {
        final Map<ItemType, Integer> neededItems = new EnumMap<>(ItemType.class);

        final int numberOfItems = 10;

        neededItems.put(ItemType.LOG, numberOfItems);
        neededItems.put(ItemType.ROCK, numberOfItems);

        return neededItems;
    }

    @Override
    public House deepClone() {
        return new House(getPosition());
    }

    @Override
    protected float getCostWhenBuilt() {
        return Float.MAX_VALUE;
    }

}
