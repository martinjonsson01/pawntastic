package com.thebois.controllers.toolbar;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.Pawntastic;
import com.thebois.controllers.AbstractInputProcessor;
import com.thebois.controllers.IController;
import com.thebois.listeners.IEventListener;
import com.thebois.listeners.events.OnClickEvent;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.StructureType;
import com.thebois.views.IProjector;
import com.thebois.views.info.IActorView;
import com.thebois.views.toolbar.BuildMenuToolbarView;

/**
 * Controller that manages the structure buttons and is responsible for placing structures.
 */
public class PlaceStructureController extends AbstractInputProcessor
    implements IEventListener<OnClickEvent<StructureType>>, IController<IActorView> {

    private static final int TILE_SIZE = Pawntastic.getTileSize();
    private final World world;
    private final IProjector projector;
    private Actor toolbarWidget;
    private StructureType selectedStructure = StructureType.TOWN_HALL;
    private final BuildMenuToolbarView structureToolbarView;

    /**
     * Instantiate a controller that manage placement of structures.
     *
     * @param world     The world where the structures should be placed.
     * @param projector Projector used to translate screen coordinates to world coordinates.
     * @param skin      The skin used to create the buttons.
     */
    public PlaceStructureController(
        final World world, final IProjector projector, final Skin skin) {
        this.world = world;
        this.projector = projector;

        structureToolbarView = new BuildMenuToolbarView(skin, this);
        updateStructureToolbarView();
    }

    @Override
    public boolean touchDown(final int x, final int y, final int pointer, final int button) {
        boolean successful = false;
        if (button == LEFT_CLICK) {
            if (!world.isTownHallPlaced()) {
                selectedStructure = StructureType.TOWN_HALL;
            }
            successful = tryPlaceStructure(x, y);

            if (world.isTownHallPlaced() && selectedStructure.equals(StructureType.TOWN_HALL)) {
                selectedStructure = StructureType.HOUSE;
            }
        }

        updateStructureToolbarView();
        return successful;
    }

    private boolean tryPlaceStructure(final int x, final int y) {
        final Vector2 worldSpaceCoordinates = projector.unproject(x, y);
        final Actor gameContainer = toolbarWidget.getParent();
        final float offsetX = gameContainer.getX();
        final float offsetY = gameContainer.getY() + toolbarWidget.getHeight();
        final int worldPosX = (int) ((worldSpaceCoordinates.x - offsetX) / TILE_SIZE);
        final int worldPosY = (int) ((worldSpaceCoordinates.y - offsetY) / TILE_SIZE);
        return world.tryCreateStructure(selectedStructure, worldPosX, worldPosY);
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
        updateStructureToolbarView();
    }

    private void updateStructureToolbarView() {
        structureToolbarView.setButtonsActive(!world.isTownHallPlaced());
    }

}
