package com.thebois;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.controllers.StructureController;
import com.thebois.controllers.TerrainController;
import com.thebois.models.world.World;
import com.thebois.views.GameScreen;
import com.thebois.views.IProjector;
import com.thebois.views.IView;
import com.thebois.views.StructureView;
import com.thebois.views.WorldView;

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    private ShapeRenderer batch;
    private BitmapFont font;
    private final int worldSize = 50;
    private final float viewportWidth = 1000f;
    private final float viewportHeight = 1000f;
    private float tileSize;
    // Models
    private World world;
    // Views
    private WorldView worldView;
    private StructureView structureView;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private TerrainController terrainController;
    private StructureController structureController;

    @Override
    public void create() {
        batch = new ShapeRenderer();

        tileSize = (float) Math.min(this.viewportHeight, this.viewportWidth) / worldSize;

        // use libGDX's default Arial font
        font = new BitmapFont();

        // Models
        world = new World(worldSize);

        // Views
        worldView = new WorldView(tileSize);
        structureView = new StructureView(tileSize);

        // Arrange Views for gameScreen
        final ArrayList<IView> views = new ArrayList<>();
        views.add(worldView);
        views.add(structureView);

        // Screens
        gameScreen = new GameScreen(batch, viewportHeight, viewportWidth, views);

        final IProjector projector = gameScreen.getProjector();
        // Controllers
        this.terrainController = new TerrainController(world, worldView);
        this.structureController = new StructureController(world,
                                                           structureView,
                                                           projector,
                                                           tileSize);

        this.setScreen(gameScreen);

        initInputProcessors();
    }

    @Override
    public void render() {
        super.render();
        terrainController.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        gameScreen.dispose();
    }

    private void initInputProcessors() {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(structureController);
        Gdx.input.setInputProcessor(multiplexer);
    }

}
