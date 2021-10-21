package com.thebois.views.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import com.thebois.models.world.structures.StructureType;

/**
 * Viewable Structure that correspond to StructureType but has view related information.
 */
public enum ViewableStructure {
    /**
     * Viewable House tile.
     */
    HOUSE(
        StructureType.HOUSE,
        new Texture(Gdx.files.internal("structures/house.png")),
        Color.valueOf("#472B14")),
    /**
     * Viewable TownHall Tile.
     */
    TOWN_HALL(
        StructureType.TOWN_HALL,
        new Texture(Gdx.files.internal("structures/town-hall.png")),
        Color.valueOf("#FFFFFF")),
    /**
     * Viewable Stockpile Type.
     */
    STOCKPILE(
        StructureType.STOCKPILE,
        new Texture(Gdx.files.internal("structures/stockpile.png")),
        Color.valueOf("#321F0E"));

    private static final Map<StructureType, ViewableStructure> BY_TYPE = new HashMap<>();

    static {
        for (final ViewableStructure structure : ViewableStructure.values()) {
            BY_TYPE.put(structure.getType(), structure);
        }
    }

    private final Color color;
    private final Texture texture;
    private final StructureType type;

    ViewableStructure(final StructureType type, final Texture texture, final Color color) {
        this.texture = texture;
        this.type = type;
        this.color = color;
    }

    /**
     * Gets a viewable representation of a given structure type.
     *
     * @param wantedType The StructureType.
     *
     * @return The ViewableStructure.
     */
    public static ViewableStructure valueByType(final StructureType wantedType) {
        return BY_TYPE.get(wantedType);
    }

    public Color getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public StructureType getType() {
        return type;
    }
}
