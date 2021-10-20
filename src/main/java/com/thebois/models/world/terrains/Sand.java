package com.thebois.models.world.terrains;

import com.thebois.models.Position;

/**
 * Terrain of type sand.
 *
 * @author Mathias
 */
public class Sand extends AbstractTerrain {

    /**
     * Instantiates a new sand terrain tile.
     *
     * @param x The X position of the tile.
     * @param y The Y position of the tile.
     */
    public Sand(final float x, final float y) {
        this(new Position(x, y));
    }

    /**
     * Instantiates a new sand terrain tile.
     *
     * @param position The position of the terrain tile.
     */
    public Sand(final Position position) {
        super(position);
    }

    @Override
    public Sand deepClone() {
        return new Sand(getPosition());
    }

    @Override
    public float getCost() {
        return 0;
    }

    @Override
    public TerrainType getType() {
        return TerrainType.SAND;
    }

}
