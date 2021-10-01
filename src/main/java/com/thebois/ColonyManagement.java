package com.thebois;

import java.util.List;

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

import com.thebois.controllers.ColonyController;
import com.thebois.controllers.RoleController;
import com.thebois.controllers.WorldController;
import com.thebois.models.world.World;
import com.thebois.views.ColonyView;
import com.thebois.views.GameScreen;
import com.thebois.views.GameView;
import com.thebois.views.IActorView;
import com.thebois.views.IProjector;
import com.thebois.views.InfoView;
import com.thebois.views.RoleView;
import com.thebois.views.ViewportWrapper;

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    public static final int WORLD_SIZE = 50;
    /* These two decide the aspect ratio that will be preserved. */
    private static final float VIEWPORT_WIDTH = 1300;
    private static final float VIEWPORT_HEIGHT = 1000;
    private static final int DEFAULT_FONT_SIZE = 26;
    private static final int PAWN_COUNT = 50;
    // LibGDX assets
    private BitmapFont font;
    private TextureAtlas skinAtlas;
    private Skin uiSkin;
    // Model
    private World world;
    /* Views - InfoView */
    private InfoView infoView;
    private RoleView roleView;
    /* Views - GameView*/
    private GameView gameView;
    private ColonyView colonyView;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private WorldController worldController;
    private ColonyController colonyController;

    @Override
    public void create() {
        final float tileSize = Math.min(VIEWPORT_HEIGHT, VIEWPORT_WIDTH) / WORLD_SIZE;

        setUpUserInterfaceSkin();

        // Model
        createModels();
        // Views
        createInfoView();

        // Camera & Vieport
        final OrthographicCamera camera = new OrthographicCamera();
        final FitViewport viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        camera.translate(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2);

        final IProjector projector = new ViewportWrapper(viewport);

        // Controllers
        this.worldController = new WorldController(world, projector, tileSize, WORLD_SIZE);

        // Screens
        gameScreen = new GameScreen(viewport,
                                    camera,
                                    uiSkin,
                                    worldController.getGameView(),
                                    infoView);
        this.setScreen(gameScreen);
        // Controllers
        createControllers(tileSize);
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

    private void createModels() {
        world = new World(WORLD_SIZE, PAWN_COUNT);
    }

    private void createInfoView() {
        roleView = new RoleView(uiSkin);
        final List<IActorView> widgetViews = List.of(roleView);
        infoView = new InfoView(widgetViews);
    }

    private void createControllers(final float tileSize) {
        new RoleController(world.getRoleAllocator(), roleView);
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
        skinAtlas.dispose();
        uiSkin.dispose();
    }

    @Override
    public void render() {
        super.render();
        world.update();
        worldController.update();
    }

    private void initInputProcessors() {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getInputProcessor());
        for (final InputProcessor inputProcessor : worldController.getInputProcessors()) {
            multiplexer.addProcessor(inputProcessor);
        }
        Gdx.input.setInputProcessor(multiplexer);
    }

}
