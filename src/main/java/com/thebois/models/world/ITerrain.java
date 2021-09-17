package com.thebois.models.world;

/**
 * Represents a tile of terrain. E.g. grass, dirt etc.
 */
public interface ITerrain extends ITile {

    /**
     * Returns the specific type of terrain.
     *
     * @return The terrain type.
     */
    TerrainType getType();
}
