package com.thebois.controllers.gamecontrollers;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.thebois.ColonyManagement;
import com.thebois.controllers.IController;
import com.thebois.models.world.World;
import com.thebois.views.IProjector;
import com.thebois.views.IView;
import com.thebois.views.debug.BeingPathDebugView;
import com.thebois.views.debug.FrameCounterView;
import com.thebois.views.gameviews.GameView;

/**
 * Container class for controllers that manage the world.
 */
public class WorldController {

    private final Collection<IController<IView>> controllers;
    private final Collection<InputProcessor> inputProcessors;
    private final GameView gameView;

    /**
     * Instantiate World controller with all controller and views used for the world.
     *
     * @param world     The world that the controllers manage.
     * @param projector Projector used for converting screen coordinates to world coordinates.
     * @param tileSize  The tile size represented on the screen.
     * @param font      The font used for game widgets.
     */
    public WorldController(
        final World world,
        final IProjector projector,
        final float tileSize,
        final BitmapFont font) {
        final StructureController structureController = new StructureController(world,
                                                                                projector,
                                                                                tileSize);
        final TerrainController terrainController = new TerrainController(world, tileSize);
        final ColonyController colonyController = new ColonyController(world, tileSize);

        // Add controllers to list.
        controllers = new ArrayList<>();
        controllers.add(terrainController);
        controllers.add(structureController);
        controllers.add(colonyController);
        // Add input processors to list.
        inputProcessors = new ArrayList<>();
        inputProcessors.add(structureController);

        gameView = createGameView(world, tileSize, font);
        structureController.setGameWidget(gameView);
    }

    private GameView createGameView(
        final World world,
        final float tileSize,
        final BitmapFont font) {
        final ArrayList<IView> views = new ArrayList<>();
        for (final IController<IView> controller : controllers) {
            views.add(controller.getIView());
        }
        if (ColonyManagement.DEBUG) {
            views.addAll(createDebugViews(world, tileSize, font));
        }
        return new GameView(views, tileSize);
    }

    private ArrayList<IView> createDebugViews(
        final World world, final float tileSize, final BitmapFont font) {
        final BeingPathDebugView beingPathDebugView = new BeingPathDebugView(world.getColony(),
                                                                             tileSize);
        final FrameCounterView frameCounterView = new FrameCounterView(font);

        final ArrayList<IView> views = new ArrayList<>();
        views.add(beingPathDebugView);
        views.add(frameCounterView);
        return views;
    }

    /**
     * Updates all controllers in the world controller.
     */
    public void update() {
        for (final IController<IView> controller : controllers) {
            controller.update();
        }
    }

    /**
     * Get the game view.
     *
     * @return The game view to be returned.
     */
    public GameView getGameView() {
        return gameView;
    }

    /**
     * Returns all Input Processors.
     *
     * @return The Input processors.
     */
    public Iterable<InputProcessor> getInputProcessors() {
        return inputProcessors;
    }

}
