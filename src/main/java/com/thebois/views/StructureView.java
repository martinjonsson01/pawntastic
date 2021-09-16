package com.thebois.views;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Responsible for displaying structures.
 */
public class StructureView {

    private final float houseWidth = 64f;
    private final float houseHeight = 64f;
    private final Color blueColor = new Color(0, 0, 1, 1);
    private ArrayList<Vector2> structures = new ArrayList<>();

    /**
     * Adds drawings to drawing Batch.
     *
     * @param batch Batch to add drawings to
     * @param delta Time since last update
     */
    public void draw(ShapeRenderer batch, float delta) {
        for (Vector2 structure : structures) {
            batch.setColor(blueColor);
            batch.rect(structure.x, structure.y, houseWidth, houseHeight);
        }
    }

    /**
     * Updates the structures that should be displayed in the game.
     *
     * @param lStructures The position of the structures.
     */
    public void updateStructureView(ArrayList<Vector2> lStructures) {
        structures = lStructures;
    }

}
