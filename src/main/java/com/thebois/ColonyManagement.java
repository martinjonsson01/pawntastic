package com.thebois;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.controllers.GameInputProcessor;
import com.thebois.views.GameScreen;

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    private ShapeRenderer batch;
    private BitmapFont font;
    private GameScreen gameScreen;

    @Override
    public void create() {
        batch = new ShapeRenderer();

        // use libGDX's default Arial font
        font = new BitmapFont();
        gameScreen = new GameScreen(batch);
        this.setScreen(gameScreen);

        initInputProcessors();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        gameScreen.dispose();
    }

    private void initInputProcessors() {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new GameInputProcessor());

        Gdx.input.setInputProcessor(multiplexer);
    }

}
