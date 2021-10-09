package com.thebois.views.info;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Represents a view that does not draw itself, but is rendered using actors instead.
 */
public interface IActorView {

    /**
     * Gets the root element of the view. The widget that contains all UI elements of the view.
     *
     * @return The root element containing all other parts of the view.
     */
    Actor getWidgetContainer();

}
