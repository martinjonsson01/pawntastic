package com.thebois.controllers;

import com.thebois.views.IView;

/**
 * Interface for controllers to expose getViews and Update method.
 */
public interface IController {

    /**
     * Get the view that the controllers updates.
     *
     * @return The view.
     */
    IView getIView();

    /**
     * Updates the part of the world that the controller manage.
     */
    void update();

}
