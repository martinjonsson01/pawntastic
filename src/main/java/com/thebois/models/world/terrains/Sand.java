package com.thebois.models.world.terrains;

import com.thebois.models.Position;

/**
 * Terrain of type sand.
 *
 * @author Mathias
 */
class Sand extends AbstractTerrain {

    /**
     * Instantiates a new sand terrain tile.
     *
     * @param position The position of the terrain tile.
     */
    Sand(final Position position) {
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
