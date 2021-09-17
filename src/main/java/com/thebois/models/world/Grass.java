package com.thebois.models.world;

import com.thebois.models.Position;

/**
 * The default world tile.
 */
public class Grass extends AbstractTerrain {

    /**
     * Instantiates a new grass terrain tile.
     *
     * @param posX The x-coordinate of the tile
     * @param posY The y-coordinate of the tile
     */
    public Grass(float posX, float posY) {
        super(posX, posY);
    }

    /**
     * Instantiates a new grass terrain tile.
     *
     * @param position The position of the tile.
     */
    public Grass(final Position position) {
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

}
