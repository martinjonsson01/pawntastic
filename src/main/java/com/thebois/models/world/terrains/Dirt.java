package com.thebois.models.world.terrains;

import com.thebois.models.Position;

/**
 * Dirt tile.
 *
 * @author Mathias
 */
public class Dirt extends AbstractTerrain {

    /**
     * Instantiates a new dirt terrain tile.
     *
     * @param x The x-coordinate of the tile
     * @param y The y-coordinate of the tile
     */
    public Dirt(final float x, final float y) {
        this(new Position(x, y));
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
