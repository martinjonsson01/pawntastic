package com.thebois.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.world.terrains.ITerrain;
import com.thebois.models.world.terrains.TerrainType;

/**
 * World view handles all the drawing of the world itself and its information on how to draw it.
 */
public class WorldView implements IView {

    private static final Color grassColor = new Color(0.005f, 0.196f, 0.107f, 1);
    private static final Color dirtColor = Color.valueOf("#bbc200");
    private final float tileSize;
    private ArrayList<ITerrain> terrainTiles = new ArrayList<>();
    private final Texture tileTexture;
    private static final Map<TerrainType, Color> TERRAIN_COLOR;

    static {
        TERRAIN_COLOR = new HashMap<>();
        TERRAIN_COLOR.put(TerrainType.GRASS, grassColor);
        TERRAIN_COLOR.put(TerrainType.DIRT, dirtColor);
    }

    /**
     * Creates the WorldView with a tileSize.
     *
     * @param tileSize The size of a single tile, in pixels.
     */
    public WorldView(final float tileSize) {
        this.tileSize = tileSize;

        tileTexture = TextureUtils.createSquareTexture(tileSize);
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
            batch.setColor(TERRAIN_COLOR.get(terrain.getType()));
            batch.draw(
                tileTexture,
                offsetX + terrain.getPosition().getPosX() * tileSize,
                offsetY + terrain.getPosition().getPosY() * tileSize,
                tileSize,
                tileSize);
        }
    }

    @Override
    public void dispose() {
        tileTexture.dispose();
    }

}
