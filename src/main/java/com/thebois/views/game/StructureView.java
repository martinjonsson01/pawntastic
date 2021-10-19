package com.thebois.views.game;

import com.badlogic.gdx.Gdx;
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

    private static final int TILE_SIZE = Pawntastic.getTileSize();
    private Iterable<IStructure> structures;
    private final Texture houseTexture;
    private final Texture ceilingTexture;
    private final Texture townHallTexture = new Texture(Gdx.files.internal("town-hall.png"));
    private final Texture defaultTexture = new Texture(Gdx.files.internal("default-structure.png"));

    /**
     * Creates an instance of a Structure view.
     */
    public StructureView() {
        houseTexture = TextureUtils.createSquareTexture(TILE_SIZE);
        ceilingTexture = TextureUtils.createTriangleTexture(TILE_SIZE);
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        for (final IStructure structure : structures) {
            switch (structure.getType()) {
                case HOUSE -> drawHouse(batch, offsetX, offsetY, structure);
                case TOWN_HALL -> drawTownHall(batch, offsetX, offsetY, structure);
                default -> drawDefault(batch, offsetX, offsetY, structure);
            }
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
            offsetX + structure.getPosition().getX() * TILE_SIZE,
            offsetY + structure.getPosition().getY() * TILE_SIZE + TILE_SIZE / 2f,
            TILE_SIZE,
            TILE_SIZE / 2f);
    }

    private void drawBase(
        final Batch batch, final float offsetX, final float offsetY, final IStructure structure) {
        batch.setColor(getHouseColor(structure));
        batch.draw(
            houseTexture,
            offsetX + structure.getPosition().getX() * TILE_SIZE,
            offsetY + structure.getPosition().getY() * TILE_SIZE,
            TILE_SIZE,
            TILE_SIZE / 2f);
    }

    // Houses get different colors based on built status
    private Color getHouseColor(final IStructure structure) {
        final Color houseColor = Color.valueOf("3B2916");
        final Color blueprintColor = Color.valueOf("1B52AB");

        return blueprintColor.lerp(houseColor, structure.getBuiltRatio());
    }

    private void drawTownHall(
        final Batch batch,
        final float offsetX,
        final float offsetY,
        final IStructure structure) {
        batch.setColor(Color.valueOf("CFCFCF"));
        batch.draw(
            townHallTexture,
            offsetX + structure.getPosition().getX() * TILE_SIZE,
            offsetY + structure.getPosition().getY() * TILE_SIZE,
            TILE_SIZE,
            TILE_SIZE);
    }

    private void drawDefault(
        final Batch batch,
        final float offsetX,
        final float offsetY,
        final IStructure structure) {
        batch.setColor(getHouseColor(structure));
        batch.draw(
            defaultTexture,
            offsetX + structure.getPosition().getX() * TILE_SIZE,
            offsetY + structure.getPosition().getY() * TILE_SIZE,
            TILE_SIZE,
            TILE_SIZE);
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
        ceilingTexture.dispose();
        townHallTexture.dispose();
    }

}
