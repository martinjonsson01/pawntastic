package com.thebois.views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The main screen displaying the world and UI.
 */
public class GameScreen implements Screen {

    private final ShapeRenderer batch;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final float worldWidth = 800f;
    private final float worldHeight = 480f;
    private final float rectangleSize = 64f;
    private final Color backgroundColor = new Color(0, 0, 0.2f, 1);
    private final int moveSpeed = 200;
    private int posY = 0;
    private final Color blueColor = new Color(0, 0, 1, 1);

    /**
     * Creates an instance of GameScreen.
     *
     * @param batch The batch to use when rendering
     */
    public GameScreen(final ShapeRenderer batch) {
        this.batch = batch;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        camera.translate(worldWidth / 2, worldHeight / 2);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(backgroundColor);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin(ShapeRenderer.ShapeType.Filled);
        batch.setColor(blueColor);
        batch.rect(worldWidth / 2 - rectangleSize, worldHeight / 2 - rectangleSize, rectangleSize,
                   rectangleSize);
        batch.setColor(1, 1, 1, 1);
        batch.rect(0, posY, rectangleSize, rectangleSize);
        batch.end();

        posY += moveSpeed * delta;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
