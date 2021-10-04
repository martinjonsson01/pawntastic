package com.thebois.controllers.game;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.game.IView;
import com.thebois.views.game.TerrainView;

/**
 * A controller used to get data about terrains and pass the data to view.
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
    public IView getView() {
        return terrainView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        terrainView.update(world.getTerrainTiles());
    }

}
