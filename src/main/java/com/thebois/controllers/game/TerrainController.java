package com.thebois.controllers.game;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.game.IView;
import com.thebois.views.game.TerrainView;

/**
 * A controller used to get data about terrains and pass the data to view.
 *
 * @author Jonathan
 */
public class TerrainController implements IController<IView> {

    private final World world;
    private final TerrainView terrainView;

    /**
     * Creates a Terrain Controller.
     *
     * @param world The world that controller should get data from.
     */
    public TerrainController(final World world) {
        this.world = world;
        this.terrainView = new TerrainView();
    }

    @Override
    public IView getView() {
        return terrainView;
    }

    @Override
    public void update() {
        terrainView.update(world.getTerrainTiles());
    }

}
