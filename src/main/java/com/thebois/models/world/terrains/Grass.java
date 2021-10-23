package com.thebois.models.world.terrains;

import com.thebois.models.Position;

/**
 * The default world tile.
 *
 * @author Jonathan
 * @author Mathias
 */
class Grass extends AbstractTerrain {

    /**
     * Instantiates a new grass terrain tile.
     *
     * @param position The position of the tile.
     */
    Grass(final Position position) {
        super(position);
    }

    @Override
    public TerrainType getType() {
        return TerrainType.GRASS;
    }

    @Override
    public Grass deepClone() {
        return new Grass(getPosition());
    }

    @Override
    public float getCost() {
        return 0;
    }

}
