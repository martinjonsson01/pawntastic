package com.thebois.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Wrapper class for viewport.
 */
class ViewportWrapper implements IProjector {

    private final Viewport viewport;

    /**
     * Creates an instance of the viewport wrapper.
     *
     * @param viewport the viewport to be wrapped.
     */
    protected ViewportWrapper(final Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public Vector2 unproject(final int screenCoordinateX, final int screenCoordinateY) {
        return viewport.unproject(new Vector2(screenCoordinateX, screenCoordinateY));
    }

}
