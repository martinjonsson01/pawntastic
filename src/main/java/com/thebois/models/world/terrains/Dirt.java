package com.thebois.models.world.terrains;

import com.thebois.models.Position;

/**
 * Dirt tile.
 *
 * @author Mathias
 */
class Dirt extends AbstractTerrain {

    /**
     * Instantiates a new dirt terrain tile.
     *
     * @param position The position of the tile.
     */
    Dirt(final Position position) {
        super(position);
    }

    @Override
    public TerrainType getType() {
        return TerrainType.DIRT;
    }

    @Override
    public Dirt deepClone() {
        return new Dirt(getPosition());
    }

    @Override
    public float getCost() {
        return 0;
    }

}
