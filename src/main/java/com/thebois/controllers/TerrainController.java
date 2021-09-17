package com.thebois.controllers;

import com.thebois.models.world.World;
import com.thebois.views.WorldView;

public class TerrainController {

    private final World world;
    private final WorldView worldView;

    public TerrainController(World world, WorldView worldView) {
        this.world = world;
        this.worldView = worldView;
    }

    public void update() {
        worldView.update(world.getTerrainTiles());
    }

}
