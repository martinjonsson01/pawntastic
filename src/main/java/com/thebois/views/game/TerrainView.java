package com.thebois.views.game;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.world.terrains.ITerrain;

/**
 * World view handles all the drawing of the world itself and its information on how to draw it.
 */
public class TerrainView implements IView {

    private Collection<ITerrain> terrainTiles = new ArrayList<>();
    private final float tileSize;

    /**
     * Creates the WorldView with a tileSize.
     *
     * @param tileSize The size of a single tile, in pixels.
     */
    public TerrainView(final float tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Creates the world grid for the view.
     *
     * @param updatedTerrainTiles Matrix of the world.
     */
    public void update(final Collection<ITerrain> updatedTerrainTiles) {
        this.terrainTiles = updatedTerrainTiles;
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        for (final ITerrain terrain : terrainTiles) {
            final ViewableTerrain viewableTerrain = ViewableTerrain.valueByType(terrain.getType());

            batch.setColor(viewableTerrain.getColor());
            batch.draw(
                viewableTerrain.getTexture(),
                offsetX + terrain.getPosition().getPosX() * tileSize,
                offsetY + terrain.getPosition().getPosY() * tileSize,
                tileSize,
                tileSize);
        }
    }

    @Override
    public void dispose() {
        for (final ViewableTerrain viewableTerrain : ViewableTerrain.values()) {
            viewableTerrain.getTexture().dispose();
        }
    }

}
