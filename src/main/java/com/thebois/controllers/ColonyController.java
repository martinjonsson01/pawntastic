package com.thebois.controllers;

import com.thebois.models.world.World;
import com.thebois.views.ColonyView;
import com.thebois.views.IView;

/**
 * Collects data form the world and sends it to its corresponding view.
 */
public class ColonyController implements IController<IView> {

    private final World world;
    private final ColonyView colonyView;

    /**
     * Creates a colony controller.
     *
     * @param world    The world that controller should get data from.
     * @param tileSize The tile size for the tiles in the world.
     */
    public ColonyController(final World world, final float tileSize) {
        this.world = world;
        this.colonyView = new ColonyView(tileSize);
    }

    @Override
    public IView getIView() {
        return colonyView;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
        colonyView.update(world.getColony());
    }

}
