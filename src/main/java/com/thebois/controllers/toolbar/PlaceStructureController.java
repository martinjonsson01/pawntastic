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
    }

    @Override
    public boolean touchDown(final int x, final int y, final int pointer, final int button) {
        boolean successful = false;
        if (button == LEFT_CLICK) {
            if (!isTownHallPlaced()) {
                selectedStructure = StructureType.TOWN_HALL;
            }
            else if (selectedStructure.equals(StructureType.TOWN_HALL)) {
                selectedStructure = StructureType.HOUSE;
            }
            successful = tryPlaceStructure(x, y, pointer);
        }
        return successful;
    }

    private boolean tryPlaceStructure(final int x, final int y, final int pointer) {
        final Vector2 worldSpaceCoordinates = projector.unproject(x, y);
        final Actor gameContainer = toolbarWidget.getParent();
        final float offsetX = gameContainer.getX();
        final float offsetY = gameContainer.getY() + toolbarWidget.getHeight();
        final int worldPosX = (int) ((worldSpaceCoordinates.x - offsetX) / TILE_SIZE);
        final int worldPosY = (int) ((worldSpaceCoordinates.y - offsetY) / TILE_SIZE);
        return world.createStructure(selectedStructure, worldPosX, worldPosY);
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
        if (isTownHallPlaced()) {
            selectedStructure = event.getValue();
        }
    }

    private boolean isTownHallPlaced() {
        return world.getStructures().stream().anyMatch(iStructure -> iStructure
            .getType()
            .equals(StructureType.TOWN_HALL));
    }

    @Override
    public IActorView getView() {
        return structureToolbarView;
    }

    @Override
    public void update() {

    }

}
