package com.thebois.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Wrapper class for viewports.
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
    public Vector2 unProject(final int screenCordX, final int screenCordY) {
        return viewport.unproject(new Vector2(screenCordX, screenCordY));
    }

}
