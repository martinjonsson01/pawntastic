package com.thebois.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The main screen displaying the world and UI.
 */
public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Color backgroundColor = new Color(0, 0, 0.2f, 1);
    private final Skin skin;
    private final GameView gameView;
    private final InfoView infoView;
    private Stage stage;
    private SpriteBatch spriteBatch;

    /**
     * Creates an instance of GameScreen.
     *
     * @param viewportHeight The height used for viewport
     * @param viewportWidth  The width used for viewport
     * @param skin           The skin with styles to apply to all widgets
     * @param gameView       The view of the game world to render
     * @param infoView       The view displaying info about the colony
     */
    public GameScreen(final float viewportHeight,
                      final float viewportWidth,
                      final Skin skin,
                      final GameView gameView,
                      final InfoView infoView) {
        this.skin = skin;
        this.gameView = gameView;
        this.infoView = infoView;

        camera = new OrthographicCamera();
        viewport = new FitViewport(viewportWidth, viewportHeight, camera);
        camera.translate(viewportWidth / 2, viewportHeight / 2);

        createStage();
    }

    private void createStage() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);

        final Table infoAndWorldGroup = new Table();
        infoAndWorldGroup.setFillParent(true);
        stage.addActor(infoAndWorldGroup);

        infoAndWorldGroup.left();

        infoAndWorldGroup.add(infoView.getPane()).expand().fill();
        infoAndWorldGroup.add(gameView);
    }

    /**
     * Gets a projector which is a wrapped viewport.
     *
     * @return The wrapped viewport as a Projector.
     */
    public IProjector getProjector() {
        return new ViewportWrapper(viewport);
    }

    /**
     * Gets a input processor for UI.
     *
     * @return The input processor.
     */
    public InputProcessor getInputProcessor() {
        return stage;
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
