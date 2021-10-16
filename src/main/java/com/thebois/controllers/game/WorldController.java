package com.thebois.controllers.game;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.thebois.Pawntastic;
import com.thebois.controllers.IController;
import com.thebois.models.beings.Colony;
import com.thebois.models.world.World;
import com.thebois.views.debug.BeingPathDebugView;
import com.thebois.views.debug.FrameCounterView;
import com.thebois.views.game.GameView;
import com.thebois.views.game.IView;

/**
 * Container class for controllers that manage the world.
 */
public class WorldController implements IController<Actor> {

    private final Collection<IController<IView>> controllers;
    private final GameView gameView;

    /**
     * Instantiate with all controllers and views used for the world.
     *
     * @param world    The world that the controllers manage.
     * @param colony   The colony that the controllers update and get information from.
     * @param tileSize The tile size represented on the screen.
     * @param font     The font used for game widgets.
     */
    public WorldController(
        final World world, final Colony colony, final float tileSize, final BitmapFont font) {
        final StructureController structureController = new StructureController(world, tileSize);
        final TerrainController terrainController = new TerrainController(world, tileSize);
        final ColonyController colonyController = new ColonyController(colony, tileSize);
        final ResourceController resourceController = new ResourceController(world, tileSize);

        controllers = List.of(terrainController,
                              resourceController,
                              structureController,
                              colonyController);

        gameView = createGameView(colony, tileSize, font);
    }

    private GameView createGameView(
        final Colony colony, final float tileSize, final BitmapFont font) {
        final List<IView> views =
            controllers.stream().map(IController::getView).collect(Collectors.toList());
        if (Pawntastic.DEBUG) {
            views.addAll(createDebugViews(colony, tileSize, font));
        }
        return new GameView(views, tileSize);
    }

    private List<IView> createDebugViews(
        final Colony colony, final float tileSize, final BitmapFont font) {
        final BeingPathDebugView beingPathDebugView = new BeingPathDebugView(colony, tileSize);
        final FrameCounterView frameCounterView = new FrameCounterView(font);

        return List.of(frameCounterView, beingPathDebugView);
    }

    @Override
    public Actor getView() {
        return gameView;
    }

    @Override
    public void update() {
        for (final IController<IView> controller : controllers) {
            controller.update();
        }
    }

}
