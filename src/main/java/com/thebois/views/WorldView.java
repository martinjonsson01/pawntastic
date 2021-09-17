package com.thebois.views;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.models.world.ITerrain;

/**
 * World view handles all the drawing of the world itself and its information on how to draw it.
 */
public class WorldView implements IView {

    private final Color grassColor = new Color(0.005f, 0.196f, 0.107f, 1);
    private ArrayList<ITerrain> terrainTiles = new ArrayList<>();
    private float tileSize;

    public WorldView(final float tileSize) {
        this.tileSize = tileSize;
    }

    /**
     * Creates the world grid for the view.
     *
     * @param updatedTerrainTiles Matrix of the world.
     */
    public void update(final ArrayList<ITerrain> updatedTerrainTiles) {
        this.terrainTiles = updatedTerrainTiles;
    }

    /**
     * Renders the world on the screen.
     *
     * @param batch Used for rendering.
     * @param tileSize Size of all tiles in the world.
     */
    @Override
    public void draw(final ShapeRenderer batch) {
        for (ITerrain terrain : terrainTiles) {
            batch.setColor(grassColor);
            batch.rect(terrain.getPosition().getPosX() * tileSize,
                       terrain.getPosition().getPosY() * tileSize,
                       tileSize,
                       tileSize);
        }
    }

}
