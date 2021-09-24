package com.thebois.views;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.world.terrains.ITerrain;

/**
 * World view handles all the drawing of the world itself and its information on how to draw it.
 */
public class WorldView implements IView {

    private final Color grassColor = new Color(0.005f, 0.196f, 0.107f, 1);
    private final float tileSize;
    private ArrayList<ITerrain> terrainTiles = new ArrayList<>();
    private Texture grassTexture;

    /**
     * Creates the WorldView with a tileSize.
     *
     * @param tileSize The size of a single tile, in pixels.
     */
    public WorldView(final float tileSize) {
        this.tileSize = tileSize;

        createGrassTexture();
    }

    private void createGrassTexture() {
        final int roundedTileSize = (int) this.tileSize;
        final Pixmap pixmap = new Pixmap(roundedTileSize, roundedTileSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(grassColor);
        pixmap.fillRectangle(0, 0, roundedTileSize, roundedTileSize);
        grassTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Creates the world grid for the view.
     *
     * @param updatedTerrainTiles Matrix of the world.
     */
    public void update(final ArrayList<ITerrain> updatedTerrainTiles) {
        this.terrainTiles = updatedTerrainTiles;
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        for (final ITerrain terrain : terrainTiles) {
            batch.draw(
                grassTexture,
                offsetX + terrain.getPosition().getPosX() * tileSize,
                offsetY + terrain.getPosition().getPosY() * tileSize,
                tileSize,
                tileSize);
        }
    }

    @Override
    public void dispose() {
        grassTexture.dispose();
    }

}
