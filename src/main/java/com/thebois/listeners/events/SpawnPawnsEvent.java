package com.thebois.listeners.events;

import com.thebois.models.Position;

/**
 *  Contains information about spawning pawns.
 */

public class SpawnPawnsEvent {

    private final int numberOfPawns;
    private final float spawnRadius;
    private final Position spawnPosition;

    /**
     * Instantiates with a position for the new obstacle.
     *
     * @param numberOfPawns The number of pawns to spawn.
     * @param spawnPosition The position to spawn pawns at.
     * @param spawnRadius The radius around the spawn position to spawn pawns.
     */
    public SpawnPawnsEvent(
        final int numberOfPawns,
        final Position spawnPosition,
        final float spawnRadius) {
        this.numberOfPawns = numberOfPawns;
        this.spawnPosition = spawnPosition;
        this.spawnRadius = spawnRadius;
    }

    public int getNumberOfPawns() {
        return this.numberOfPawns;
    }

    public float getSpawnRadius() {
        return spawnRadius;
    }

    public Position getSpawnPosition() {
        return spawnPosition;
    }

}
