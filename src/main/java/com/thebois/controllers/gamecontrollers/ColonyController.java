package com.thebois.controllers.gamecontrollers;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.gameviews.IView;
import com.thebois.views.gameviews.ColonyView;

/**
 * Collects data form the world and sends it to its corresponding view.
 */
public class ColonyController implements IController<IView> {

    private final World world;
    private final ColonyView colonyView;

    /**
     * Creates a colony controller.
     *
     * @param world    The world to get colony from.
     * @param tileSize The tile size for the tiles in the world.
     */
    public ColonyController(final World world, final float tileSize) {
        this.world = world;
        this.colonyView = new ColonyView(tileSize);
    }

    @Override
    public IView getView() {
        return colonyView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        colonyView.update(world.getColony());
    }

}
