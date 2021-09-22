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
    public House(final int posX, final int posY) {
        super(posX, posY, StructureType.HOUSE);
    }

    /**
     * Creates a house structure at a given position in the world.
     *
     * @param position The position of the house
     */
    public House(Position position) {
        super(position, StructureType.HOUSE);
    }

    @Override
    public House deepClone() {
        return new House(getPosition());
    }

}
