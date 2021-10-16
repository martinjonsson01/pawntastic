package com.thebois.models.world.structures;

import com.thebois.models.Position;

/**
 * A factory that instantiates structures.
 */
public final class StructureFactory {

    private StructureFactory() {

    }

    /**
     * Creates an instance of a structure with given type at the given position.
     *
     * @param type     The type of structure to be created.
     * @param position The position of the structure.
     *
     * @return The created structure
     */
    public static IStructure createStructure(
        final StructureType type, final Position position) {

        return switch (type) {
            case HOUSE -> new House(position);
        };
    }

    /**
     * Creates an instance of a structure with given type at the given position.
     *
     * @param type The type of structure to be created.
     * @param x    The X position of the structure.
     * @param y    The Y position of the structure.
     *
     * @return The created structure
     */
    public static IStructure createStructure(final StructureType type, final int x, final int y) {
        return createStructure(type, new Position(x, y));
    }

}
