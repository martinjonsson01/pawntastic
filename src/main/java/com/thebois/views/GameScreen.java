package com.thebois.views;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.thebois.views.info.InfoView;
import com.thebois.views.toolbar.ToolbarView;

/**
 * The main screen displaying the world and UI.
 */
public class GameScreen implements Screen {

    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final Color backgroundColor = new Color(0, 0, 0.2f, 1);
    private final Actor gameView;
    private final InfoView infoView;
    private final ToolbarView toolbarView;
    private Stage stage;
    private SpriteBatch spriteBatch;
    private final Actor gameAndToolbarContainer;

    /**
     * Creates an instance of GameScreen.
     *
     * @param viewport    The viewport for the game.
     * @param camera      The camera used to display the game.
     * @param gameView    The view of the game world to render
     * @param infoView    The view displaying info about the colony
     * @param toolbarView The view that displays the tool bar.
     */
    public GameScreen(
        final FitViewport viewport,
        final OrthographicCamera camera,
        final Actor gameView,
        final InfoView infoView,
        final ToolbarView toolbarView) {
        this.viewport = viewport;
        this.camera = camera;
        this.gameView = gameView;
        this.infoView = infoView;
        this.toolbarView = toolbarView;
        this.gameAndToolbarContainer = createGameAndToolBarContainer();

        createStage();
    }

    private void createStage() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);

        final Table infoAndGameTable = new Table();
        infoAndGameTable.setFillParent(true);
        infoAndGameTable.add(infoView.getPane()).fillY().expandY();
        infoAndGameTable.add(gameAndToolbarContainer).top().left().expandY().fillY();
        // Add to stage
        stage.addActor(infoAndGameTable);
    }

    private Table createGameAndToolBarContainer() {
        final Table gameContainerTable = new Table();
        gameContainerTable.add(gameView).top().left();
        gameContainerTable.row().left().bottom().expandY().fillY();
        gameContainerTable.add(toolbarView.getPane()).fill();
        return gameContainerTable;
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
