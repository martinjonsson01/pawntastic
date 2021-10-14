package com.thebois.views.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.views.TextureUtils;

/**
 * View that renders the resources that exist in the world.
 */
public class ResourceView implements IView {

    private static final Color WATER_COLOR = Color.valueOf("#1E90FF");
    private static final Color TREE_COLOR = Color.valueOf("#006400");
    private static final Color ROCK_COLOR = Color.valueOf("#808080");
    private final Map<ResourceType, Texture> resourceTextureMap;
    private final float tileSize;
    private Iterable<IResource> resourceTiles = new ArrayList<>();

    /**
     * Creates the Resource view.
     *
     * @param tileSize The size of a single tile, in pixels.
     */
    public ResourceView(final float tileSize) {
        this.tileSize = tileSize;
        resourceTextureMap = new HashMap<>();
        resourceTextureMap.put(ResourceType.WATER,
                               TextureUtils.createSquareTexture(tileSize, WATER_COLOR));
        resourceTextureMap.put(ResourceType.TREE,
                               TextureUtils.createTriangleTexture(tileSize, TREE_COLOR));
        resourceTextureMap.put(ResourceType.STONE,
                               TextureUtils.createTriangleTexture(tileSize, ROCK_COLOR));
    }

    /**
     * Updates the resource grid for the view.
     *
     * @param updatedResourceGrid Matrix of the world.
     */
    public void update(final Iterable<IResource> updatedResourceGrid) {
        this.resourceTiles = updatedResourceGrid;
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        for (final IResource resource : resourceTiles) {
            batch.setColor(Color.WHITE);
            batch.draw(resourceTextureMap.get(resource.getType()),
                       offsetX + resource.getPosition().getPosX() * tileSize,
                       offsetY + resource.getPosition().getPosY() * tileSize,
                       tileSize,
                       tileSize);
        }
    }

    @Override
    public void dispose() {
        resourceTextureMap.values().forEach(Texture::dispose);
    }

}
