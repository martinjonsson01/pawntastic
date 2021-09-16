package com.thebois.views;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.thebois.models.World;
import com.thebois.models.tiles.ITile;

/**
 * The main screen displaying the world and UI.
 */
public class GameScreen implements Screen {

    private final ShapeRenderer batch;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final float worldWidth = 1000f;
    private final float worldHeight = 1000f;
    private final float rectangleSize = 20f;
    private final Color backgroundColor = new Color(0, 0, 0.2f, 1);
    private final int moveSpeed = 200;
    private int moveY = 0;
    private final Color blueColor = new Color(0, 0, 1, 1);
    private final Color grassColor = new Color(0.005f, 0.196f, 0.107f, 1);
    private final int worldSize = 50;
    private final ITile[][] worldGrid;

    /**
     * Creates an instance of GameScreen.
     *
     * @param batch The batch to use when rendering
     */
    public GameScreen(final ShapeRenderer batch) {
        this.batch = batch;

        worldGrid = new World(worldSize).getWorld();

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

        renderWorld();

        batch.begin(ShapeRenderer.ShapeType.Filled);
        batch.setColor(blueColor);
        batch.rect(worldWidth / 2 - rectangleSize, worldHeight / 2 - rectangleSize, rectangleSize,
                   rectangleSize);
        batch.setColor(1, 1, 1, 1);
        batch.rect(0, moveY, rectangleSize, rectangleSize);
        batch.end();

        moveY += moveSpeed * delta;
    }

    private void renderWorld() {
        batch.begin(ShapeRenderer.ShapeType.Filled);
        batch.setColor(grassColor);
        for (int posX = 0; posX < worldGrid.length; posX++) {
            for (int posY = 0; posY < worldGrid[0].length; posY++) {
                batch.rect(posX * rectangleSize, posY * rectangleSize, rectangleSize,
                           rectangleSize);
            }
        }
        batch.end();
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
