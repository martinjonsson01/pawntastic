package com.thebois;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.models.world.World;
import com.thebois.views.GameScreen;
import com.thebois.views.WorldView;

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    private ShapeRenderer batch;
    private BitmapFont font;
    private final int worldSize = 50;

    // Models
    private World world;

    // Views
    private WorldView worldView;

    // Screens
    private GameScreen gameScreen;

    // Controllers

    @Override
    public void create() {
        batch = new ShapeRenderer();

        // use libGDX's default Arial font
        font = new BitmapFont();

        // Models
        world = new World(worldSize);

        // Views
        worldView = new WorldView(world.getWorld());

        // Screens
        gameScreen = new GameScreen(batch, worldView, worldSize);

        // Controllers

        this.setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        gameScreen.dispose();
    }

}
