package com.thebois.models.world.structures;

import java.util.Collections;

import com.thebois.models.Position;

/**
 * The colony's main building.
 *
 * @author Jacob
 */
public class TownHall extends AbstractStructure {

    protected TownHall(
        final Position position) {
        super(position, StructureType.TOWN_HALL, Collections.emptyMap());
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
