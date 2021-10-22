package com.thebois.models.world.terrains;

import com.thebois.models.Position;

/**
 * A factory that creates terrain tiles.
 */
public final class TerrainFactory {

    private TerrainFactory() {

    }

    /**
     * Generate a terrain with given type at the given position.
     *
     * @param type What type of terrain.
     * @param position The position of the terrain.
     *
     * @return The created terrain.
     *
     * @throws NullPointerException If the type of resource is null.
     */
    public static ITerrain createTerrain(final TerrainType type, final Position position) {
        return switch (type) {
            case GRASS -> new Grass(position);
            case SAND -> new Sand(position);
            case DIRT -> new Dirt(position);
        };
    }

    /**
     * Creates an instance of a terrain with given type at the given position.
     *
     * @param type The type of terrain to be created.
     * @param x    The X position of the terrain.
     * @param y    The Y position of the terrain.
     *
     * @return The created terrain.
     */
    public static ITerrain createTerrain(final TerrainType type, final int x, final int y) {
        return createTerrain(type, new Position(x, y));
    }

}
