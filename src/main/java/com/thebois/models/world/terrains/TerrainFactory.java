package com.thebois.models.world.terrains;

/**
 * A factory that creates terrain view.
 */
public final class TerrainFactory {

    private TerrainFactory() {

    }

    /**
     * Generate a terrain with given type at given position.
     *
     * @param type What type of terrain.
     * @param x    X position for terrain.
     * @param y    Y position for terrain.
     *
     * @return The created terrain.
     */
    public static ITerrain createTerrain(final TerrainType type, final int x, final int y) {
        if (type == TerrainType.DIRT) {
            return new Dirt(x, y);
        }
        else if (type == TerrainType.SAND) {
            return new Sand(x, y);
        }
        else {
            return new Grass(x, y);
        }
    }

}
