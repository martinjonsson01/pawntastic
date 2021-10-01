package com.thebois.controllers;

import com.thebois.models.world.World;
import com.thebois.views.gameviews.ColonyView;

/**
 * Collects data form the world and sends it to its corresponding view.
 */
public class ColonyController {

    private final World world;
    private final ColonyView colonyView;

    /**
     * Creates a colony controller.
     *
     * @param world      The world that controller should get data from.
     * @param colonyView The view the controller updates.
     */
    public ColonyController(World world, ColonyView colonyView) {
        this.world = world;
        this.colonyView = colonyView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        colonyView.update(world.getColony());
    }

}
