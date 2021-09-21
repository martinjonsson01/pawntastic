package com.thebois.controllers;

import com.badlogic.gdx.math.Vector2;

import com.thebois.models.world.World;
import com.thebois.views.IProjector;
import com.thebois.views.StructureView;

/**
 * Controller for Structures in the world.
 */
public class StructureController extends AbstractInputProcessor {

    private final World world;
    private final StructureView structureView;
    private final IProjector projector;
    private final float tileSize;

    /**
     * Creates a instance of a Structure Controller.
     *
     * @param world         The world in which the structures exists.
     * @param structureView A view where the structures will be displayed.
     * @param projector     Projector used for converting screen coordinates to world coordinates.
     * @param tileSize      The tile size represented on the screen.
     */
    public StructureController(final World world,
                               final StructureView structureView,
                               final IProjector projector,
                               float tileSize) {

        this.world = world;
        this.structureView = structureView;
        this.projector = projector;
        this.tileSize = tileSize;

        structureView.update(world.getStructures());
    }

    @Override
    public boolean touchDown(final int x, final int y, final int pointer, final int button) {
        if (button == LEFT_CLICK) {

            final Vector2 worldSpaceCords = projector.unproject(x, y);
            final int worldPosX = (int) (worldSpaceCords.x / tileSize);
            final int worldPosY = (int) (worldSpaceCords.y / tileSize);
            if (world.createStructure(worldPosX, worldPosY)) {
                structureView.update(world.getStructures());
                return true;
            }
        }
        return false;
    }

}
