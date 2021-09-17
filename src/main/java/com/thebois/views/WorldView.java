package com.thebois.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.models.tiles.ITile;

/**
 * World view handles all the drawing of the world itself and its information on how to draw it.
 */
public class WorldView {

    private final Color grassColor = new Color(0.005f, 0.196f, 0.107f, 1);
    private ITile[][] worldGrid;

    private WorldView() {
    }

    /**
     * Creates the world grid for the view.
     *
     * @param worldGrid Matrix of the world.
     */
    public WorldView(final ITile[][] worldGrid) {
        this.worldGrid = worldGrid;
    }

    /**
     * Renders the world on the screen.
     *
     * @param batch Used for rendering.
     * @param rectangleSize Size of all rectangles in the world.
     */
    public void draw(final ShapeRenderer batch, final float rectangleSize) {
        for (int posX = 0; posX < worldGrid.length; posX++) {
            for (int posY = 0; posY < worldGrid[0].length; posY++) {
                batch.setColor(grassColor);
                batch.rect(posX * rectangleSize, posY * rectangleSize, rectangleSize,
                           rectangleSize);
            }
        }
    }
}
