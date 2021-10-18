package com.thebois;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.common.eventbus.EventBus;

import com.thebois.controllers.game.WorldController;
import com.thebois.controllers.info.InfoController;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.world.World;
import com.thebois.persistence.LoadSystem;
import com.thebois.persistence.SaveSystem;
import com.thebois.views.GameScreen;
import com.thebois.views.IProjector;
import com.thebois.views.ViewportWrapper;

/**
 * The main representation of the game.
 */
public class Pawntastic extends Game {

    private static final EventBus BUS = new EventBus();
    /* Toggles debug-mode. */
    private static final boolean DEBUG = false;
    private static final int WORLD_SIZE = 50;
    /* These two decide the aspect ratio that will be preserved. */
    private static final float VIEWPORT_WIDTH = 1300;
    private static final float VIEWPORT_HEIGHT = 1000;
    private static final int DEFAULT_FONT_SIZE = 26;
    private static final int PAWN_POSITIONS = 50;
    private static final int TILE_SIZE = (int) Math.min(VIEWPORT_HEIGHT, VIEWPORT_WIDTH)
                                         / WORLD_SIZE;
    // LibGDX assets
    private BitmapFont font;
    private TextureAtlas skinAtlas;
    private Skin uiSkin;
    // Model
    private World world;
    private Colony colony;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private WorldController worldController;
    private InfoController infoController;

    /**
     * Gets the size of a single tile in world space.
     *
     * @return The tile size.
     */
    public static int getTileSize() {
        return TILE_SIZE;
    }

    /**
     * Gets the number of tiles per axis in the world.
     *
     * @return The number of tiles.
     */
    public static int getWorldSize() {
        return WORLD_SIZE;
    }

    /**
     * Whether or not the game is run in debug mode or not.
     *
     * @return Whether or not the game is running in debug mode.
     */
    public static boolean isDebugEnabled() {
        return DEBUG;
    }

    /**
     * Gets the global event bus that most events pass through.
     *
     * @return The event bus.
     */
    public static EventBus getEventBus() {
        return BUS;
    }

    @Override
    public void create() {

        setUpUserInterfaceSkin();

        // Model
        try {
            createModels();
        }
        catch (final IOException | ClassNotFoundException error) {
            error.printStackTrace();
        }
        // Camera & Viewport
        final OrthographicCamera camera = new OrthographicCamera();
        final FitViewport viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        camera.translate(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2);

        final IProjector projector = new ViewportWrapper(viewport);

        // Controllers
        this.worldController = new WorldController(world, colony, projector, font);
        this.infoController = new InfoController(colony, uiSkin);

        // Screens
        gameScreen = new GameScreen(viewport,
                                    camera,
                                    uiSkin,
                                    worldController.getView(),
                                    infoController.getView());
        this.setScreen(gameScreen);

        // Set up Input Processors
        initInputProcessors();
    }

    private void setUpUserInterfaceSkin() {
        // Load LibGDX assets.
        generateFont();
        uiSkin = new Skin();
        uiSkin.add("default-font", font);
        skinAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        uiSkin.addRegions(skinAtlas);
        uiSkin.load(Gdx.files.internal("uiskin.json"));
    }

    private void createModels() throws IOException, ClassNotFoundException {
        try {
            loadModelsFromSaveFile();
        }
        catch (final IOException exception) {
            world = new World(WORLD_SIZE, 0);
            colony = new Colony(
                world.findEmptyPositions(PAWN_POSITIONS),
                new AstarPathFinder(world), world, world);
        }
    }

    private void initInputProcessors() {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getInputProcessor());
        for (final InputProcessor inputProcessor : worldController.getInputProcessors()) {
            multiplexer.addProcessor(inputProcessor);
        }
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void generateFont() {
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(
            "fonts/arial.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.size = DEFAULT_FONT_SIZE;
        generator.scaleForPixelHeight(DEFAULT_FONT_SIZE);
        font = generator.generateFont(parameter);
        // To smooth out the text.
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                                                Texture.TextureFilter.Linear);
        generator.dispose();
    }

    private void loadModelsFromSaveFile() throws IOException, ClassNotFoundException {
        final LoadSystem loadSystem = new LoadSystem();
        world = loadSystem.loadWorld();
        colony = loadSystem.loadColony();
        loadSystem.dispose();
    }

    @Override
    public void dispose() {
        try {
            saveModelsToSaveFile();
        }
        catch (final IOException error) {
            error.printStackTrace();
        }

        gameScreen.dispose();
        skinAtlas.dispose();
        uiSkin.dispose();
    }

    private void saveModelsToSaveFile() throws IOException {
        final SaveSystem saveSystem = new SaveSystem();
        saveSystem.save(world);
        saveSystem.save(colony);
        saveSystem.dispose();
    }

    @Override
    public void render() {
        super.render();
        colony.update(Gdx.graphics.getDeltaTime());
        worldController.update();
        infoController.update();
    }

}
