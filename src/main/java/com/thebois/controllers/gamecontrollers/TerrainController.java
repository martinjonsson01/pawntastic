package com.thebois.controllers.gamecontrollers;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.IView;
import com.thebois.views.gameviews.WorldView;

/**
 * A controller that gathers data from the world and updates the view.
 */
public class TerrainController implements IController<IView> {

    private final World world;
    private final WorldView worldView;

    /**
     * Creates a Terrain Controller.
     *
     * @param world    The world that controller should get data from.
     * @param tileSize The tile size of the world.
     */
    public TerrainController(final World world, final float tileSize) {
        this.world = world;
        this.worldView = new WorldView(tileSize);
    }

    @Override
    public IView getIView() {
        return worldView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        worldView.update(world.getTerrainTiles());
    }

}
