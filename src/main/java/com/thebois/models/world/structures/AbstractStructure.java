package com.thebois.models.world.structures;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * Base structure for the game.
 */
abstract class AbstractStructure implements IStructure {

    private Position position;
    private StructureType structureType;

    /**
     * Creates a structure with a position.
     *
     * @param posX          Position in X-axis
     * @param posY          Position in Y-axis
     * @param structureType The type of structure to create.
     */
    AbstractStructure(float posX, float posY, StructureType structureType) {
        this.position = new Position(posX, posY);
        this.structureType = structureType;
    }

    /**
     * Creates a structure with a position and type.
     *
     * @param position      The position the structure have.
     * @param structureType The type of structure to create.
     */
    AbstractStructure(Position position, StructureType structureType) {
        this(position.getPosX(), position.getPosY(), structureType);
    }

    /**
     * Creates a structure with a position.
     *
     * @param posX Position in X-axis
     * @param posY Position in Y-axis
     */
    AbstractStructure(float posX, float posY) {
        this(posX, posY, StructureType.HOUSE);
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AbstractStructure structure = (AbstractStructure) o;
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
