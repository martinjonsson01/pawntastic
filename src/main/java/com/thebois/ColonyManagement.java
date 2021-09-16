package com.thebois;

import com.thebois.views.GameScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        gameScreen.dispose();
    }

}
