package com.thebois.controllers.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import com.thebois.Pawntastic;
import com.thebois.controllers.AbstractInputProcessor;
import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.StructureType;
import com.thebois.views.IProjector;
import com.thebois.views.game.IView;
import com.thebois.views.game.StructureView;

/**
 * Controller for Structures in the world.
 */
public class StructureController extends AbstractInputProcessor implements IController<IView> {

    private final World world;
    private final StructureView structureView;
    private final IProjector projector;
    private final int tileSize = Pawntastic.getTileSize();
    private Widget gameWidget;

    /**
     * Creates a instance of a Structure Controller.
     *
     * @param world     The world in which the structures exists.
     * @param projector Projector used for converting screen coordinates to world coordinates.
     */
    public StructureController(
        final World world, final IProjector projector) {

        this.world = world;
        this.structureView = new StructureView();
        this.projector = projector;

        structureView.update(world.getStructures());
    }

    @Override
    public boolean touchDown(final int x, final int y, final int pointer, final int button) {
        if (button == LEFT_CLICK) {
            final Vector2 worldSpaceCoordinates = projector.unproject(x, y);
            final float offSetX = gameWidget.getX();
            final float offSetY = gameWidget.getY();
            final int worldX = (int) ((worldSpaceCoordinates.x - offSetX) / tileSize);
            final int worldY = (int) ((worldSpaceCoordinates.y - offSetY) / tileSize);
            if (world.createStructure(StructureType.HOUSE, worldX, worldY)) {
                structureView.update(world.getStructures());
                return true;
            }
        }
        return false;
    }

    @Override
    public IView getView() {
        return structureView;
    }

    @Override
    public void update() {
    }

    /**
     * Set the game widget used to get the offset.
     *
     * @param gameWidget The widget in which the game is rendered.
     */
    public void setGameWidget(final Widget gameWidget) {
        this.gameWidget = gameWidget;
    }

}
