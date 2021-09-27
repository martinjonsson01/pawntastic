package com.thebois.models.world.structures;

import com.thebois.models.Position;
import com.thebois.models.world.inventory.IItem;

/**
 * A structure of type House.
 */
public class House extends AbstractStructure {

    private float builtRatio = 0;

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
    public House(final Position position) {
        super(position, StructureType.HOUSE);
    }

    @Override
    public boolean deliverItem(final IItem deliveredItem) {
        // Temporary variable to show update of buildings
        final float buildFrequency = 0.01f;

        // builtRatio = (builtRatio + buildFrequency) % 1f;
        builtRatio = Math.min(builtRatio + buildFrequency, 1);
        return true;
    }

    @Override
    public float builtStatus() {
        return builtRatio;
    }

    @Override
    public House deepClone() {
        return new House(getPosition());
    }

}
