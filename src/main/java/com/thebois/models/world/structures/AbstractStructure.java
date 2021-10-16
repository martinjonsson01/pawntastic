package com.thebois.models.world.structures;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * Base structure for the game.
 */
abstract class AbstractStructure implements IStructure {

    private final Position position;
    private final StructureType structureType;

    /**
     * Creates a structure with a position and structure type.
     *
     * @param posX          Position in X-axis
     * @param posY          Position in Y-axis
     * @param structureType The type of structure to create.
     */
    protected AbstractStructure(final int posX, final int posY, final StructureType structureType) {
        this.position = new Position(posX, posY);
        this.structureType = structureType;
    }

    /**
     * Creates a structure with a position and type.
     *
     * @param position      The position the structure have.
     * @param structureType The type of structure to create.
     */
    protected AbstractStructure(final Position position, final StructureType structureType) {
        this((int) position.getPosX(), (int) position.getPosY(), structureType);
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
        final AbstractStructure that = (AbstractStructure) o;
        return position.equals(that.position) && structureType == that.structureType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, structureType);
    }

    @Override
    public StructureType getType() {
        return structureType;
    }

}
