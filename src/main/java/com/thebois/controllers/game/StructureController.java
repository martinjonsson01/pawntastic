package com.thebois.controllers.game;

import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.game.IView;
import com.thebois.views.game.StructureView;

/**
 * Controller for Structures in the world.
 */
public class StructureController implements IController<IView> {

    private final World world;
    private final StructureView structureView;

    /**
     * Creates a instance of a Structure Controller.
     *
     * @param world    The world in which the structures exists.
     * @param tileSize The tile size represented on the screen.
     */
    public StructureController(
        final World world, final float tileSize) {

        this.world = world;
        this.structureView = new StructureView(tileSize);
        structureView.update(world.getStructures());
    }

    @Override
    public IView getView() {
        return structureView;
    }

    @Override
    public void update() {
        structureView.update(world.getStructures());
    }

}
