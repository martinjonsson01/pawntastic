package com.thebois.models.world.structures;

import java.util.Collections;

import com.thebois.models.Position;
import com.thebois.models.inventory.IInventory;

/**
 * The colony's main building.
 *
 * @author jacob
 */
public class TownHall extends Stockpile {

    protected TownHall(
        final Position position, final IInventory inventory) {
        super(position, StructureType.TOWN_HALL, Collections.emptyMap(), inventory);
        postStructureCompletedEvent();
    }

    // The town hall is always completely built.
    @Override
    public float getBuiltRatio() {
        return 1f;
    }

    // The town hall is always completed.
    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    protected float getCostWhenBuilt() {
        return Float.MAX_VALUE;
    }

}
