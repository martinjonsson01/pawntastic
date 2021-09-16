package com.thebois;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.models.World;
import com.thebois.views.GameScreen;
import com.thebois.views.WorldView;

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    private ShapeRenderer batch;
    private BitmapFont font;
    private GameScreen gameScreen;
    private World world;
    private WorldView worldView;
    private final int worldSize = 50;

    @Override
    public void create() {
        batch = new ShapeRenderer();

        // use libGDX's default Arial font
        font = new BitmapFont();
        world = new World(worldSize);
        worldView = new WorldView(world.getWorld());
        gameScreen = new GameScreen(batch, worldView);

        this.setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        gameScreen.dispose();
    }

}
