package com.thebois.controllers;

import com.thebois.models.beings.Colony;
import com.thebois.views.gameviews.ColonyView;

/**
 * Collects data form the world and sends it to its corresponding view.
 */
public class ColonyController {

    private final Colony colony;
    private final ColonyView colonyView;

    /**
     * Creates a colony controller.
     *
     * @param colony     The colony that controller should get data from.
     * @param colonyView The view the controller updates.
     */
    public ColonyController(final Colony colony, final ColonyView colonyView) {
        this.colony = colony;
        this.colonyView = colonyView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        colonyView.update(colony);
    }

}
