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
    private final float viewportWidth = 1000f;
    private final float viewportHeight = 1000f;
    private final float rectangleSize;
    private final Color backgroundColor = new Color(0, 0, 0.2f, 1);
    private final int moveSpeed = 200;
    private int posY = 0;
    private final Color blueColor = new Color(0, 0, 1, 1);
    private WorldView worldView;

    /**
     * Creates an instance of GameScreen.
     *
     * @param batch The batch to use when rendering
     * @param worldView The view of the world with information on how to draw itself.
     * @param worldSize The size of the matrix for the world.
     */
    public GameScreen(final ShapeRenderer batch, WorldView worldView, final int worldSize) {
        this.batch = batch;

        this.worldView = worldView;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        viewport = new FitViewport(viewportWidth, viewportHeight, camera);
        camera.translate(viewportWidth / 2, viewportHeight / 2);
        rectangleSize = (float) Math.min(viewportHeight, viewportWidth) / worldSize;
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

        // render views
        worldView.draw(batch, rectangleSize);

        batch.setColor(blueColor);
        batch.rect(viewportWidth / 2 - rectangleSize, viewportHeight / 2 - rectangleSize,
                   rectangleSize, rectangleSize);
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
