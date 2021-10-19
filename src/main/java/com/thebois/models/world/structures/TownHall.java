package com.thebois.models.world.structures;

import java.util.Collections;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.SpawnPawnsEvent;
import com.thebois.models.Position;

/**
 * The colony's main building.
 */
public class TownHall extends AbstractStructure {

    private static final int NUMBER_OF_STARTING_PAWNS = 6;

    protected TownHall(
        final Position position) {
        super(position, StructureType.TOWN_HALL, Collections.emptyMap());
        postSpawnPawnsEvent(position);
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
    public IStructure deepClone() {
        return new TownHall(getPosition());
    }

    @Override
    public float getCost() {
        return Float.MAX_VALUE;
    }

    private void postSpawnPawnsEvent(final Position spawnPosition) {
        final SpawnPawnsEvent spawnPawnsEvent = new SpawnPawnsEvent(
            NUMBER_OF_STARTING_PAWNS,
            spawnPosition,
            2f);
        Pawntastic.getEventBus().post(spawnPawnsEvent);
    }

}
