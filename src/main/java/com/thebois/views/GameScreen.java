package com.thebois.views;

import java.util.Collection;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The main screen displaying the world and UI.
 */
public class GameScreen implements Screen {

    private final ShapeRenderer batch;
    private final BitmapFont font;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final float viewportWidth;
    private final float viewportHeight;
    private final Color backgroundColor = new Color(0, 0, 0.2f, 1);
    private final Collection<IView> views;

    /**
     * Creates an instance of GameScreen.
     *
     * @param batch          The batch to use when rendering
     * @param font           The font for all text in the game
     * @param viewportHeight The height used for viewport
     * @param viewportWidth  The width used for viewport
     * @param views          Views to draw
     */
    public GameScreen(final ShapeRenderer batch,
                      final BitmapFont font,
                      final float viewportHeight,
                      final float viewportWidth,
                      final Collection<IView> views) {
        this.batch = batch;
        this.font = font;

        this.viewportHeight = viewportHeight;
        this.viewportWidth = viewportWidth;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        viewport = new FitViewport(this.viewportWidth, this.viewportHeight, camera);
        camera.translate(this.viewportWidth / 2, this.viewportHeight / 2);

        this.views = views;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(backgroundColor);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin(ShapeRenderer.ShapeType.Filled);

        // render views
        for (final IView view : views) {
            view.draw(batch);
        }
        batch.end();
    }

    @Override
    public void resize(final int width, final int height) {
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
