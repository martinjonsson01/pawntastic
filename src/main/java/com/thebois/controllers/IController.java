package com.thebois.controllers;

/**
 * Interface for controllers to expose getViews and Update method.
 *
 * @param <TView> What kind of view the controller manage, i.e IView for game and IActorView for
 *                info panels.
 */
public interface IController<TView> {

    /**
     * Get the view that the controllers updates.
     *
     * @return The view.
     */
    TView getIView();

    /**
     * Updates the part of the world that the controller manage.
     */
    void update();

}
