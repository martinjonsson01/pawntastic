package com.thebois.views;

import java.util.Random;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.IBeing;

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
    private ColonyView colonyView;
    private Colony colony;
    private final Random random = new Random();

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

        initColony();
    }

    /**
     * This method is temporary do not forget to remove!!!.
     */
    void initColony() {
        // Change this variable to spawn a different amount of pawns on the screen.
        final int numberOfPawns = 1000;

        colony = new Colony();

        for (int i = 0; i < numberOfPawns; i++) {
            colony.createBeing(new Position(random.nextInt((int) worldWidth * 2),
                                            random.nextInt((int) worldWidth * 2)));
        }

        for (IBeing pawn : colony.getBeings()) {
            pawn.walkTo(new Position(random.nextInt((int) worldWidth),
                                     random.nextInt((int) worldWidth)));
        }

        colonyView = new ColonyView();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(backgroundColor);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        colony.update();
        colonyView.drawAllPawns(batch, colony.getBeings());
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
