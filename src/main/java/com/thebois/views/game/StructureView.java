package com.thebois.views.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.Pawntastic;
import com.thebois.models.world.structures.IStructure;

/**
 * Responsible for displaying structures.
 *
 * @author Jacob
 * @author Mathias
 */
public final class StructureView implements IView {

    /**
     * Missing texture applied when image has not been added for specific enum.
     */
    private static final Texture MISSING_TEXTURE = new Texture(Gdx.files.internal(
        "missing-texture.png"));
    private static final Texture BLUEPRINT_TEXTURE = new Texture(Gdx.files.internal(
        "structures/blueprint.png"));
    private static final int TILE_SIZE = Pawntastic.getTileSize();
    private Iterable<IStructure> structures;

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        Texture textureToDraw;
        for (final IStructure structure : structures) {
            final ViewableStructure viewableStructure =
                ViewableStructure.valueByType(structure.getType());

            if (structure.isCompleted()) {
                textureToDraw = viewableStructure.getTexture();
            }
            else {
                textureToDraw = BLUEPRINT_TEXTURE;
            }

            batch.setColor(calculateColor(viewableStructure.getColor(), structure.getBuiltRatio()));
            batch.draw(
                textureToDraw,
                offsetX + structure.getPosition().getX() * TILE_SIZE,
                offsetY + structure.getPosition().getY() * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE);
        }
    }

    // Houses get different colors based on built status
    private Color calculateColor(final Color structureColor, final float builtRatio) {
        final Color blueprintColor = Color.valueOf("1B52AB");
        return blueprintColor.lerp(structureColor, builtRatio);
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
        for (final ViewableStructure viewableStructure : ViewableStructure.values()) {
            viewableStructure.getTexture().dispose();
        }
        MISSING_TEXTURE.dispose();
    }

}
