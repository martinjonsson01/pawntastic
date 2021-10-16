package com.thebois.views;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.thebois.models.world.structures.StructureType;
import com.thebois.views.game.GameView;
import com.thebois.views.game.StructureButton;
import com.thebois.views.info.InfoView;

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
    private Table toolbarAndGame;
    private final float toolBarHeight;

    /**
     * Creates an instance of GameScreen.
     *
     * @param viewport      The viewport for the game.
     * @param camera        The camera used to display the game.
     * @param skin          The skin with styles to apply to all widgets
     * @param gameView      The view of the game world to render
     * @param infoView      The view displaying info about the colony
     * @param toolBarHeight The height of the tool bar.
     */
    public GameScreen(
        final FitViewport viewport,
        final OrthographicCamera camera,
        final Skin skin,
        final GameView gameView,
        final InfoView infoView,
        final float toolBarHeight) {
        this.viewport = viewport;
        this.camera = camera;
        this.skin = skin;
        this.gameView = gameView;
        this.infoView = infoView;
        this.toolBarHeight = toolBarHeight;

        createStage();
    }

    /*
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

     */

    private void createStage() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);

        final Table toolBar = createToolBar();

        toolbarAndGame = new Table();
        toolbarAndGame.add(gameView);
        toolbarAndGame.row().height(toolBarHeight).left();
        toolbarAndGame.add(toolBar);

        final Table infoAndGameTable = new Table();
        infoAndGameTable.setFillParent(true);
        infoAndGameTable.add(infoView.getPane()).fill().expand();
        infoAndGameTable.add(toolbarAndGame);
        // Add to stage
        stage.addActor(infoAndGameTable);
    }

    private Table createToolBar() {
        // Set up table
        final Table toolBarTable = new Table();
        toolBarTable.left().top();
        toolBarTable.row().expandY().fillY();
        // Button group settings
        final ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);
        // Add buttons to table and button group
        for (final StructureType type : StructureType.values()) {
            final Button structureButton = new StructureButton(type, skin);
            toolBarTable.add(structureButton);
            buttonGroup.add(structureButton);
        }
        return toolBarTable;
    }

    /**
     * Gets a input processor for UI.
     *
     * @return The input processor.
     */
    public InputProcessor getInputProcessor() {
        return stage;
    }

    public Table getTable() {
        return toolbarAndGame;
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
