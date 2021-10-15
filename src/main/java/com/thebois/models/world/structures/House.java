package com.thebois.models.world.structures;

import com.thebois.models.Position;

/**
 * A structure of type House.
 */
public class House extends AbstractStructure {

    /**
     * Creates a house structure at a given position in the world.
     *
     * @param posX Position in X-axis
     * @param posY Position in Y-axis
     */
    House(final int posX, final int posY) {
        super(posX, posY, StructureType.HOUSE);
    }

    /**
     * Creates a house structure at a given position in the world.
     *
     * @param position The position of the house
     */
    House(final Position position) {
        super(position, StructureType.HOUSE);
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
