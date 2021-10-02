package com.thebois.views.gameviews;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.world.structures.IStructure;
import com.thebois.views.IView;
import com.thebois.views.TextureUtils;

/**
 * Responsible for displaying structures.
 */
public final class StructureView implements IView {

    private final float tileSize;
    private Iterable<IStructure> structures;
    private final Color houseColor = new Color(0.4f, 0.2f, 0f, 1);
    private Texture houseTexture;

    /**
     * Creates an instance of a Structure view.
     *
     * @param tileSize The size of a single world tile in screen space.
     */
    public StructureView(final float tileSize) {
        this.tileSize = tileSize;

        houseTexture = TextureUtils.createSquareTexture(tileSize);
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        batch.setColor(houseColor);
        for (final IStructure structure : structures) {
            batch.setColor(houseColor);
            batch.draw(
                houseTexture,
                offsetX + structure.getPosition().getPosX() * tileSize,
                offsetY + structure.getPosition().getPosY() * tileSize,
                tileSize,
                tileSize);
        }
    }

    /**
     * Updates the structures that should be displayed in the game.
     *
     * @param updatedStructures The structures that are to be displayed.
     */
    public void update(final Iterable<IStructure> updatedStructures) {
        this.structures = updatedStructures;
    }

    @Override
    public void dispose() {
        houseTexture.dispose();
    }

}
