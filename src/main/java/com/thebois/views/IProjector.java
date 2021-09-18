package com.thebois.views;

import com.badlogic.gdx.math.Vector2;

/**
 * Interface for a Projector.
 */
public interface IProjector {

    /**
     * Unprojects screen coordiantes to world coordinates.
     *
     * @param screenCordX X coordinate of the screen.
     * @param screenCordY Y coordinate of the screen.
     *
     * @return Returns a Vector2 with the world coordinates.
     */
    Vector2 unProject(int screenCordX, int screenCordY);

}
