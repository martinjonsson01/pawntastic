package com.thebois.controllers;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.InputProcessor;

import com.thebois.models.world.World;
import com.thebois.views.GameView;
import com.thebois.views.IProjector;
import com.thebois.views.IView;

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
     * @param worldSize The size of the world, number of tiles per row.
     */
    public WorldController(final World world,
                           final IProjector projector,
                           final float tileSize,
                           final int worldSize) {
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

        gameView = createGameView(tileSize, worldSize);
        structureController.setGameWidget(gameView);
    }

    private GameView createGameView(final float tileSize, final int worldSize) {
        final ArrayList<IView> views = new ArrayList<>();
        for (final IController<IView> controller : controllers) {
            views.add(controller.getIView());
        }
        return new GameView(views, worldSize, tileSize);
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
