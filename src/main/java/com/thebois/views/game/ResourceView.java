package com.thebois.views.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.Pawntastic;
import com.thebois.models.world.resources.IResource;

/**
 * View that renders the resources that exist in the world.
 */
public class ResourceView implements IView {

    private static final int TILE_SIZE = Pawntastic.getTileSize();
    private Iterable<IResource> resourceTiles = new ArrayList<>();

    /**
     * Creates the Resource view.
     */
    public ResourceView() {
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
            final ViewableResource viewableResource =
                ViewableResource.valueByType(resource.getType());
            batch.setColor(Color.WHITE);
            batch.draw(
                viewableResource.getTexture(),
                offsetX + resource.getPosition().getX() * TILE_SIZE,
                offsetY + resource.getPosition().getY() * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE);
        }
    }

    @Override
    public void dispose() {
        for (final ViewableResource resource : ViewableResource.values()) {
            resource.getTexture().dispose();
        }
    }

}
