package com.thebois;

import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.ColonyController;
import com.thebois.controllers.RoleController;
import com.thebois.controllers.StructureController;
import com.thebois.controllers.TerrainController;
import com.thebois.models.world.World;
import com.thebois.views.gameviews.ColonyView;
import com.thebois.views.GameScreen;
import com.thebois.views.gameviews.GameView;
import com.thebois.views.infoviews.IActorView;
import com.thebois.views.IView;
import com.thebois.views.infoviews.InfoView;
import com.thebois.views.infoviews.RoleView;
import com.thebois.views.gameviews.StructureView;
import com.thebois.views.gameviews.WorldView;
import com.thebois.views.infoviews.ColonyInventoryView;

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    private static final int WORLD_SIZE = 50;
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
    private ColonyInventoryView colonyInventoryView;
    /* Views - GameView*/
    private GameView gameView;
    private WorldView worldView;
    private StructureView structureView;
    private ColonyView colonyView;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private TerrainController terrainController;
    private StructureController structureController;
    private ColonyController colonyController;

    @Override
    public void create() {
        final float tileSize = Math.min(VIEWPORT_HEIGHT, VIEWPORT_WIDTH) / WORLD_SIZE;

        setUpUserInterfaceSkin();

        // Model
        createModels();
        // Views
        createGameView(tileSize);
        createInfoView();
        // Screens
        gameScreen = new GameScreen(VIEWPORT_HEIGHT, VIEWPORT_WIDTH, uiSkin, gameView, infoView);
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

    private void createGameView(final float tileSize) {
        worldView = new WorldView(tileSize);
        structureView = new StructureView(tileSize);

        // Arrange Views for gameScreen
        colonyView = new ColonyView(tileSize);
        final List<IView> views = List.of(worldView, colonyView, structureView);
        gameView = new GameView(views, WORLD_SIZE, tileSize);
    }

    private void createInfoView() {
        roleView = new RoleView(uiSkin);
        final List<IActorView> widgetViews = List.of(roleView);
        infoView = new InfoView(widgetViews);
    }

    private void createControllers(final float tileSize) {
        this.terrainController = new TerrainController(world, worldView);
        this.structureController = new StructureController(world,
                                                           structureView,
                                                           gameScreen.getProjector(),
                                                           tileSize,
                                                           gameView);
        this.colonyController = new ColonyController(world, colonyView);
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
        terrainController.update();
        colonyController.update();
        structureController.update();
    }

    private void initInputProcessors() {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getInputProcessor());
        multiplexer.addProcessor(structureController);
        Gdx.input.setInputProcessor(multiplexer);
    }

}
