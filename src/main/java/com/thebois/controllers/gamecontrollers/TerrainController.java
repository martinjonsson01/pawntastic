package com.thebois.controllers.gamecontrollers;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.gameviews.IView;
import com.thebois.views.gameviews.TerrainView;

/**
 * A controller that gathers data from the world and updates the view.
 */
public class TerrainController implements IController<IView> {

    private final World world;
    private final TerrainView terrainView;

    /**
     * Creates a Terrain Controller.
     *
     * @param world    The world that controller should get data from.
     * @param tileSize The tile size of the world.
     */
    public TerrainController(final World world, final float tileSize) {
        this.world = world;
        this.terrainView = new TerrainView(tileSize);
    }

    @Override
    public IView getIView() {
        return terrainView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        terrainView.update(world.getTerrainTiles());
    }

}
