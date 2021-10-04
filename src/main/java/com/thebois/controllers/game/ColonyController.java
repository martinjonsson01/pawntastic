package com.thebois.controllers.game;

import com.thebois.controllers.IController;
import com.thebois.models.beings.AbstractBeingGroup;
import com.thebois.views.game.ColonyView;
import com.thebois.views.game.IView;

/**
 * Collects data form the world and sends it to its corresponding view.
 */
public class ColonyController implements IController<IView> {

    private final AbstractBeingGroup colony;
    private final ColonyView colonyView;

    /**
     * Creates a colony controller.
     *
     * @param colony   The colony that should be updated and displayed.
     * @param tileSize The tile size for the tiles in the world.
     */
    public ColonyController(final AbstractBeingGroup colony, final float tileSize) {
        this.colony = colony;
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
        colonyView.update(colony);
    }

}
