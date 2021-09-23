package com.thebois.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

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
    private final Widget gameWidget;

    /**
     * Creates a instance of a Structure Controller.
     *
     * @param world         The world in which the structures exists.
     * @param structureView A view where the structures will be displayed.
     * @param projector     Projector used for converting screen coordinates to world coordinates.
     * @param tileSize      The tile size represented on the screen.
     * @param gameWidget    The widget in which the game in rendered in.
     */
    public StructureController(final World world,
                               final StructureView structureView,
                               final IProjector projector,
                               float tileSize,
                               Widget gameWidget) {

        this.world = world;
        this.structureView = structureView;
        this.projector = projector;
        this.tileSize = tileSize;
        this.gameWidget = gameWidget;

        structureView.update(world.getStructures());
    }

    @Override
    public boolean touchDown(final int x, final int y, final int pointer, final int button) {
        if (button == LEFT_CLICK) {
            final Vector2 worldSpaceCoordinates = projector.unproject(x, y);
            final float offSetX = gameWidget.getX();
            final float offSetY = gameWidget.getY();
            final int worldPosX = (int) ((worldSpaceCoordinates.x - offSetX) / tileSize);
            final int worldPosY = (int) ((worldSpaceCoordinates.y - offSetY) / tileSize);
            if (world.createStructure(worldPosX, worldPosY)) {
                structureView.update(world.getStructures());
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the view with data from the world.
     */
    public void update() {
    }

}
