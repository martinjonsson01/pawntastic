package com.thebois.views;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Ensures views can be rendered.
 */
public interface IView {

    /**
     * Used for view when rendering.
     *
     * @param batch Render all shapes in the provided batch.
     */
    void draw(ShapeRenderer batch);

}
