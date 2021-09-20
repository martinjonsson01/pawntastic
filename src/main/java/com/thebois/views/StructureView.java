package com.thebois.views;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.models.world.structures.IStructure;

/**
 * Responsible for displaying structures.
 */
public final class StructureView implements IView {

    private final float tileSize;
    private ArrayList<IStructure> structures = new ArrayList<>();
    private final Color houseColor = new Color(0.4f, 0.2f, 0f, 1);

    /**
     * Creates an instance of a Structure view.
     *
     * @param tileSize The size of a single world tile in screen space.
     */
    public StructureView(final float tileSize) {
        this.tileSize = tileSize;
    }

    @Override
    public void draw(ShapeRenderer batch) {
        for (IStructure structure : structures) {
            batch.setColor(houseColor);
            final int posX = (int) ((structure.getPosition().getPosX()) * tileSize);
            final int poxY = (int) ((structure.getPosition().getPosY()) * tileSize);
            batch.rect(posX, poxY, tileSize, tileSize);
        }
    }

    /**
     * Gets the tile size of the world.
     *
     * @return the tile size as a float.
     */
    public float getTileSize() {
        return tileSize;
    }

    /**
     * Updates the structures that should be displayed in the game.
     *
     * @param updatedStructures The structures that are to be displayed.
     */
    public void update(ArrayList<IStructure> updatedStructures) {
        this.structures = updatedStructures;
    }

}
