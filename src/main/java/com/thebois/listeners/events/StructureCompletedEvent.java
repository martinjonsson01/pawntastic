package com.thebois.listeners.events;

import com.thebois.models.Position;
import com.thebois.models.world.structures.StructureType;

/**
 *  Contains information about spawning pawns.
 */
public class StructureCompletedEvent {

    private final StructureType structureType;
    private final Position position;

    /**
     * Instantiates with information to be sent in the StructureCompletedEvent.
     *
     * @param structureType The type of completed structure.
     * @param position The position of the completed structure.
     */
    public StructureCompletedEvent(
        final StructureType structureType, final Position position) {
        this.structureType = structureType;
        this.position = position;
    }

    public StructureType getStructureType() {
        return structureType;
    }

    public Position getPosition() {
        return position;
    }

}
