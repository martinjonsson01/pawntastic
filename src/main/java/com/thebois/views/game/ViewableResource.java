package com.thebois.views.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import com.thebois.models.world.resources.ResourceType;
import com.thebois.views.TextureUtils;

/**
 * Viewable Resource that correspond to ResourceType but has view related information.
 *
 * @author Mathias
 */
public enum ViewableResource {
    /**
     * Viewable Water tile.
     */
    WATER(ResourceType.WATER, TextureUtils.createSquareTexture(Color.valueOf("#1E90FF"))),
    /**
     * Viewable Tree tile.
     */
    TREE(ResourceType.TREE, TextureUtils.createTriangleTexture(Color.valueOf("#006400"))),
    /**
     * Viewable Rock tile.
     */
    ROCK(ResourceType.ROCK, TextureUtils.createTriangleTexture(Color.valueOf("#808080")));
    private static final Map<ResourceType, ViewableResource> BY_TYPE = new HashMap<>();

    static {
        for (final ViewableResource resource : ViewableResource.values()) {
            BY_TYPE.put(resource.getType(), resource);
        }
    }

    private final Texture texture;
    private final ResourceType type;

    ViewableResource(final ResourceType type, final Texture texture) {
        this.texture = texture;
        this.type = type;
    }

    /**
     * Gets a graphical representation of a given type of resource.
     *
     * @param wantedType The ResourceType.
     *
     * @return The ViewableResource.
     */
    public static ViewableResource valueByType(final ResourceType wantedType) {
        return BY_TYPE.get(wantedType);
    }

    public Texture getTexture() {
        return texture;
    }

    public ResourceType getType() {
        return type;
    }
}
