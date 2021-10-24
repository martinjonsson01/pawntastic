package com.thebois.controllers;

/**
 * Represent an updater for a view.
 *
 * @param <TView> What kind of view the controller manages, i.e IView for game and IActorView for
 *                info panels.
 *
 * @author Mathias
 */
public interface IController<TView> {

    /**
     * Get the view that the controller updates.
     *
     * @return The view.
     */
    TView getView();

    /**
     * Updates the part of the world that the controller manage.
     */
    void update();

}
