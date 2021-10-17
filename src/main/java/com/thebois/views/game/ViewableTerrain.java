package com.thebois.views.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import com.thebois.models.world.terrains.TerrainType;
import com.thebois.views.TextureUtils;

/**
 * Viewable Terrain that correspond to TerrainType but has view related information.
 */
public enum ViewableTerrain {
    /**
     * Viewable Grass tile.
     */
    GRASS(
        TerrainType.GRASS,
        TextureUtils.createSquareTexture(),
        Color.valueOf("#008000")),
    /**
     * Viewable Sand Tile.
     */
    SAND(
        TerrainType.SAND,
        TextureUtils.createSquareTexture(),
        Color.valueOf("#bbc201")),
    /**
     * Viewable Sand Type.
     */
    DIRT(
        TerrainType.DIRT,
        TextureUtils.createSquareTexture(),
        Color.valueOf("#76552b"));
    private static final Map<TerrainType, ViewableTerrain> BY_TYPE = new HashMap<>();

    static {
        for (final ViewableTerrain terrain : ViewableTerrain.values()) {
            BY_TYPE.put(terrain.getType(), terrain);
        }
    }

    private final Color color;
    private final Texture texture;
    private final TerrainType type;

    ViewableTerrain(final TerrainType type, final Texture texture, final Color color) {
        this.texture = texture;
        this.type = type;
        this.color = color;
    }

    /**
     * Gets a viewable representation of a given terrain type.
     *
     * @param wantedType The TerrainType.
     *
     * @return The ViewableTerrain.
     */
    public static ViewableTerrain valueByType(final TerrainType wantedType) {
        return BY_TYPE.get(wantedType);
    }

    public Color getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public TerrainType getType() {
        return type;
    }
}
