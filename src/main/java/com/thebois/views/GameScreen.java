package com.thebois.views;

import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The main screen displaying the world and UI.
 */
public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Color backgroundColor = new Color(0, 0, 0.2f, 1);
    private final int worldSize;
    private final float tileSize;
    private final Collection<IView> views;
    private Stage stage;
    private SpriteBatch spriteBatch;

    /**
     * Creates an instance of GameScreen.
     *
     * @param viewportHeight The height used for viewport
     * @param viewportWidth  The width used for viewport
     * @param worldSize      The size of the world in tiles
     * @param tileSize       The size of a single tile in world space
     * @param views          Views to draw
     */
    public GameScreen(final float viewportHeight,
                      final float viewportWidth,
                      final int worldSize,
                      final float tileSize,
                      final Collection<IView> views) {
        this.worldSize = worldSize;
        this.tileSize = tileSize;
        this.views = views;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        viewport = new FitViewport(viewportWidth, viewportHeight, camera);
        camera.translate(viewportWidth / 2, viewportHeight / 2);

        createStage();
    }

    private void createStage() {
        // UI Stage
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        final HorizontalGroup group = new HorizontalGroup();
        group.setFillParent(true);
        stage.addActor(group);

        group.setDebug(true);

        final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.addRegions(atlas);

        group.center();

        final TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        final TextButton button1 = new TextButton("Button 1", buttonStyle);
        group.addActor(button1);

        group.addActor(new Widget() {
            @Override
            public float getPrefWidth() {
                return tileSize * worldSize;
            }

            @Override
            public float getPrefHeight() {
                return getPrefWidth();
            }

            @Override
            public void draw(final Batch batch, final float parentAlpha) {
                // render views
                for (final IView view : views) {
                    view.draw(batch, getX(), getY());
                }
            }
        });
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        ScreenUtils.clear(backgroundColor);
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);

        // Render UI stage
        stage.act();
        stage.draw();
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
        stage.dispose();
    }

}
