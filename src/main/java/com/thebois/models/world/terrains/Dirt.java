package com.thebois.models.world.terrains;

import com.thebois.models.Position;

/**
 * Dirt tile.
 */
public class Dirt extends AbstractTerrain {

    /**
     * Instantiates a new dirt terrain tile.
     *
     * @param posX The x-coordinate of the tile
     * @param posY The y-coordinate of the tile
     */
    public Dirt(float posX, float posY) {
        super(posX, posY);
    }

    /**
     * Instantiates a new dirt terrain tile.
     *
     * @param position The position of the tile.
     */
    public Dirt(final Position position) {
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
