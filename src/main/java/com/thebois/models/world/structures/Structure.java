package com.thebois.models.world.structures;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * Base structure for the game.
 */
public class Structure implements IStructure {

    private Position position;
    private StructureType structureType;

    /**
     * Creates a structure with a position.
     *
     * @param position      The position the structure have.
     * @param structureType The type of structures to create.
     */
    public Structure(Position position, StructureType structureType) {
        this.position = new Position(position.getPosX(), position.getPosY());
        this.structureType = structureType;
    }

    /**
     * Creates a structure with a position.
     *
     * @param posX Position in X-axis
     * @param posY Position in Y-axis
     */
    public Structure(int posX, int posY) {
        this.position = new Position(posX, posY);
    }

    @Override
    public Position getPosition() {
        return new Position(position.getPosX(), position.getPosY());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Structure structure = (Structure) o;
        return Objects.equals(position, structure.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public StructureType getType() {
        return structureType;
    }

}
