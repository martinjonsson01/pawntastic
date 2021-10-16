package com.thebois.controllers.toolbar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.AbstractInputProcessor;
import com.thebois.controllers.IController;
import com.thebois.listeners.IEventListener;
import com.thebois.listeners.events.OnClickEvent;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.StructureType;
import com.thebois.views.IProjector;
import com.thebois.views.info.IActorView;
import com.thebois.views.toolbar.StructureToolbarView;

/**
 * Controller that manages the structure buttons on the toolbar and is responsible for placing
 * structures.
 */
public class PlaceStructureController extends AbstractInputProcessor
    implements IEventListener<OnClickEvent<StructureType>>, IController<IActorView> {

    private final World world;
    private final IProjector projector;
    private final float tileSize;
    private Actor toolbarWidget;
    private StructureType selectedStructure = StructureType.HOUSE;
    private final StructureToolbarView structureToolbarView;

    /**
     * Instantiate a controller that manage placement of structures.
     *
     * @param world     The world where the structures should be placed.
     * @param projector Projector used to translate screen coordinates to world coordinates.
     * @param tileSize  The tile size of the world.
     * @param skin      The skin used to create the buttons used to select what structure to build.
     */
    public PlaceStructureController(
        final World world, final IProjector projector, final float tileSize, final Skin skin) {
        this.world = world;
        this.projector = projector;
        this.tileSize = tileSize;

        structureToolbarView = new StructureToolbarView(skin, this);
    }

    @Override
    public boolean touchDown(final int x, final int y, final int pointer, final int button) {
        if (button == LEFT_CLICK) {
            final Vector2 worldSpaceCoordinates = projector.unproject(x, y);
            final Actor gameContainer = toolbarWidget.getParent();
            final float offSetX = gameContainer.getX();
            final float offSetY = gameContainer.getY() + toolbarWidget.getHeight();
            final int worldPosX = (int) ((worldSpaceCoordinates.x - offSetX) / tileSize);
            final int worldPosY = (int) ((worldSpaceCoordinates.y - offSetY) / tileSize);
            return world.createStructure(selectedStructure, worldPosX, worldPosY);
        }
        return false;
    }

    /**
     * Set the game widget used to get the offset.
     *
     * @param toolbarWidget The widget in which the game is rendered.
     */
    public void setToolbarWidget(final Actor toolbarWidget) {
        this.toolbarWidget = toolbarWidget;
    }

    @Override
    public void onEvent(final OnClickEvent<StructureType> event) {
        selectedStructure = event.getValue();
    }

    @Override
    public IActorView getView() {
        return structureToolbarView;
    }

    @Override
    public void update() {

    }

}
