package com.thebois.views.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Ensures views can be rendered.
 *
 * @author Martin
 */
public interface IView extends Disposable {

    /**
     * Used for view when rendering.
     *
     * @param batch   Render all shapes in the provided batch.
     * @param offsetX The x-coordinate that the view is offset by.
     * @param offsetY The y-coordinate that the view if offset by.
     */
    void draw(Batch batch, float offsetX, float offsetY);

}
