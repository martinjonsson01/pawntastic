package com.thebois.views.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.Pawntastic;
import com.thebois.models.world.structures.IStructure;
import com.thebois.views.TextureUtils;

/**
 * Responsible for displaying structures.
 */
public final class StructureView implements IView {

    private Iterable<IStructure> structures;
    private final Color houseColor = Color.valueOf("#CD853F");
    private final Texture houseTexture;
    private final Texture ceilingTexture;

    /**
     * Creates an instance of a Structure view.
     */
    public StructureView() {
        houseTexture = TextureUtils.createSquareTexture();
        ceilingTexture = TextureUtils.createTriangleTexture();
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        batch.setColor(houseColor);
        for (final IStructure structure : structures) {
            drawHouse(batch, offsetX, offsetY, structure);
        }
    }

    private void drawHouse(
        final Batch batch, final float offsetX, final float offsetY, final IStructure structure) {
        drawBase(batch, offsetX, offsetY, structure);
        drawRoof(batch, offsetX, offsetY, structure);
    }

    private void drawRoof(
        final Batch batch, final float offsetX, final float offsetY, final IStructure structure) {
        batch.setColor(Color.BLACK);
        batch.draw(
            ceilingTexture,
            offsetX + structure.getPosition().getPosX() * Pawntastic.getTileSize(),
            offsetY
            + structure.getPosition().getPosY() * Pawntastic.getTileSize()
            + Pawntastic.getTileSize() / 2f,
            Pawntastic.getTileSize(),
            Pawntastic.getTileSize() / 2f);
    }

    private void drawBase(
        final Batch batch, final float offsetX, final float offsetY, final IStructure structure) {
        batch.setColor(houseColor);
        batch.draw(
            houseTexture,
            offsetX + structure.getPosition().getPosX() * Pawntastic.getTileSize(),
            offsetY + structure.getPosition().getPosY() * Pawntastic.getTileSize(),
            Pawntastic.getTileSize(),
            Pawntastic.getTileSize() / 2f);
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
