package com.thebois.views;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Ensures views can be rendered.
 */
public interface IView {

    /**
     * Used for view when rendering.
     *
     * @param batch   Render all shapes in the provided batch.
     * @param offsetX The offsetX-coordinate that the view is offset by
     * @param offsetY The y-coordinate that the view if offset by
     */
    void draw(Batch batch, float offsetX, float offsetY);

}
