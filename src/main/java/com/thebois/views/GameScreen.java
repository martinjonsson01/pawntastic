package com.thebois.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private Stage stage;
    private SpriteBatch spriteBatch;

    /**
     * Creates an instance of GameScreen.
     *
     * @param viewportHeight The height used for viewport
     * @param viewportWidth  The width used for viewport
     * @param skin           The skin with styles to apply to all widgets
     * @param gameView       The view of the game world to render
     */
    public GameScreen(final float viewportHeight,
                      final float viewportWidth,
                      final Skin skin,
                      final GameView gameView) {
        this.skin = skin;
        this.gameView = gameView;

        camera = new OrthographicCamera();
        viewport = new FitViewport(viewportWidth, viewportHeight, camera);
        camera.translate(viewportWidth / 2, viewportHeight / 2);

        createStage();
    }

    private void createStage() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(viewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);

        final Table infoAndWorldGroup = new Table();
        infoAndWorldGroup.setFillParent(true);
        stage.addActor(infoAndWorldGroup);

        infoAndWorldGroup.left();

        final TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        final TextButton button1 = new TextButton("Button 1", buttonStyle);
        final TextButton button2 = new TextButton("Button 2", buttonStyle);
        final TextButton button3 = new TextButton("Button 3", buttonStyle);

        final VerticalGroup infoPaneGroup = new VerticalGroup();
        infoPaneGroup.addActor(button1);
        infoPaneGroup.addActor(button2);
        infoPaneGroup.addActor(button3);

        final Container<VerticalGroup> infoPane = new Container<>(infoPaneGroup).fill();
        infoPane.setBackground(createBackground(Color.BLUE));

        infoAndWorldGroup.add(infoPane).expand().fill();
        infoAndWorldGroup.add(gameView);
    }

    private Drawable createBackground(final Color color) {
        final Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(color);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
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
