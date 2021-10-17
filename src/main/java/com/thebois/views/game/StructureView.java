package com.thebois.views.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.world.structures.IStructure;
import com.thebois.views.TextureUtils;

/**
 * Responsible for displaying structures.
 */
public final class StructureView implements IView {

    private final float tileSize;
    private Iterable<IStructure> structures;
    private final Color houseColor = Color.valueOf("#CD853F");
    private final Texture houseTexture;
    private final Texture ceilingTexture;

    /**
     * Creates an instance of a Structure view.
     *
     * @param tileSize The size of a single world tile in screen space.
     */
    public StructureView(final float tileSize) {
        this.tileSize = tileSize;

        houseTexture = TextureUtils.createSquareTexture((int) tileSize);
        ceilingTexture = TextureUtils.createTriangleTexture((int) tileSize);
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
            offsetX + structure.getPosition().getPosX() * tileSize,
            offsetY + structure.getPosition().getPosY() * tileSize + tileSize / 2,
            tileSize,
            tileSize / 2);
    }

    private void drawBase(
        final Batch batch, final float offsetX, final float offsetY, final IStructure structure) {
        batch.setColor(getHouseColor(structure));
        batch.draw(
            houseTexture,
            offsetX + structure.getPosition().getPosX() * tileSize,
            offsetY + structure.getPosition().getPosY() * tileSize,
            tileSize,
            tileSize / 2);
    }

    // Houses get different colors based on built status
    private Color getHouseColor(final IStructure structure) {
        final Color blueprintColor = Color.valueOf("3B2916");
        final Color houseColor = Color.valueOf("1B52AB");

        return houseColor.lerp(blueprintColor, structure.getBuiltRatio());
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
