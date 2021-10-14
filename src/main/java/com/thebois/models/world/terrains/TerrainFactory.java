package com.thebois.models.world.terrains;

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
     * @param x    X position for terrain.
     * @param y    Y position for terrain.
     *
     * @return The created terrain.
     *
     * @throws NullPointerException If the type of resource is null.
     */
    public static ITerrain createTerrain(final TerrainType type, final int x, final int y) {
        return switch (type) {
            case GRASS -> new Grass(x, y);
            case SAND -> new Sand(x, y);
            case DIRT -> new Dirt(x, y);
        };
    }

}
