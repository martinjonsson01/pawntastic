package com.thebois.controllers;

import com.thebois.models.world.World;
import com.thebois.views.gameviews.WorldView;

/**
 * A controller that gathers data from the world and updates the view.
 */
public class TerrainController {

    private final World world;
    private final WorldView worldView;

    /**
     * Creates a Terrain Controller.
     *
     * @param world     The world that controller should get data from.
     * @param worldView The view the controller updates.
     */
    public TerrainController(World world, WorldView worldView) {
        this.world = world;
        this.worldView = worldView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        worldView.update(world.getTerrainTiles());
    }

}
