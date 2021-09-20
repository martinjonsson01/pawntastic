package com.thebois;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.controllers.ColonyController;
import com.thebois.controllers.TerrainController;
import com.thebois.models.world.World;
import com.thebois.views.ColonyView;
import com.thebois.views.GameScreen;
import com.thebois.views.IView;
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
    // Models
    private World world;
    // Views
    private WorldView worldView;
    private ColonyView colonyView;
    // Screens
    private GameScreen gameScreen;
    private TerrainController terrainController;
    private float tileSize;
    // Controllers
    private ColonyController colonyController;

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
        colonyView = new ColonyView();
        final ArrayList<IView> views = new ArrayList<>();
        views.add(worldView);
        views.add(colonyView);

        // Controllers
        this.terrainController = new TerrainController(world, worldView);
        this.colonyController = new ColonyController(world, colonyView);

        // Screens
        gameScreen = new GameScreen(batch, viewportHeight, viewportWidth, views);

        this.setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
        terrainController.update();
        colonyController.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        gameScreen.dispose();
    }

}
