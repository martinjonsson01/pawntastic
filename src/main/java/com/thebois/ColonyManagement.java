package com.thebois;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.eventbus.EventBus;

import com.thebois.controllers.ColonyController;
import com.thebois.controllers.InventoryController;
import com.thebois.controllers.RoleController;
import com.thebois.controllers.StructureController;
import com.thebois.controllers.TerrainController;
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.Pawn;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
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
import com.thebois.views.infoviews.InventoryView;
import com.thebois.views.debug.BeingPathDebugView;
import com.thebois.views.debug.FrameCounterView;

/**
 * The main representation of the game.
 */
public class ColonyManagement extends Game {

    /**
     * The global event bus that most events pass through.
     */
    public static final EventBus BUS = new EventBus();
    private static final int WORLD_SIZE = 50;
    /* These two decide the aspect ratio that will be preserved. */
    private static final float VIEWPORT_WIDTH = 1300;
    private static final float VIEWPORT_HEIGHT = 1000;
    private static final int DEFAULT_FONT_SIZE = 26;
    private static final int PAWN_POSITIONS = 50;
    /* Toggles debug-mode. */
    private static final boolean DEBUG = false;
    // LibGDX assets
    private BitmapFont font;
    private TextureAtlas skinAtlas;
    private Skin uiSkin;
    // Model
    private World world;
    private Colony colony;
    /* Views - InfoView */
    private InfoView infoView;
    private RoleView roleView;
    private InventoryView inventoryView;
    /* Views - GameView*/
    private GameView gameView;
    private WorldView worldView;
    private StructureView structureView;
    private ColonyView colonyView;
    /* Views - GameView DEBUG */
    private BeingPathDebugView beingPathDebugView;
    private FrameCounterView frameCounterView;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private TerrainController terrainController;
    private StructureController structureController;
    private ColonyController colonyController;
    private InventoryController inventoryController;
    private float tileSize;

    @Override
    public void create() {
        tileSize = Math.min(VIEWPORT_HEIGHT, VIEWPORT_WIDTH) / WORLD_SIZE;

        setUpUserInterfaceSkin();

        // Model
        createModels();

        // Views
        if (DEBUG) createDebugView();
        createGameView();
        createInfoView();

        // Screens
        gameScreen = new GameScreen(VIEWPORT_HEIGHT, VIEWPORT_WIDTH, uiSkin, gameView, infoView);

        this.setScreen(gameScreen);

        // Controllers
        createControllers();
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

    private void createModels() {
        world = new World(WORLD_SIZE);
        colony = new Colony(world.findEmptyPositions(PAWN_POSITIONS), new AstarPathFinder(world));
    }

    private void createDebugView() {
        beingPathDebugView = new BeingPathDebugView(colony, tileSize);
        frameCounterView = new FrameCounterView(font);
    }

    private void createGameView() {
        worldView = new WorldView(tileSize);

        // Arrange Views for gameScreen
        colonyView = new ColonyView(tileSize);
        structureView = new StructureView(tileSize);
        final List<IView> views = new ArrayList<>();
        views.add(worldView);
        views.add(colonyView);
        views.add(structureView);
        if (DEBUG) {
            views.add(beingPathDebugView);
            views.add(frameCounterView);
        }
        gameView = new GameView(views, WORLD_SIZE, tileSize);
    }

    private void createInfoView() {
        roleView = new RoleView(uiSkin);
        inventoryView = new InventoryView(uiSkin);
        final List<IActorView> widgetViews = List.of(roleView, inventoryView);
        infoView = new InfoView(widgetViews);
    }

    private void createControllers() {
        this.terrainController = new TerrainController(world, worldView);
        this.structureController = new StructureController(world,
                                                           structureView,
                                                           gameScreen.getProjector(),
                                                           tileSize,
                                                           gameView);
        this.colonyController = new ColonyController(colony, colonyView);
        this.inventoryController = new InventoryController(inventoryView, colony.getInventory());
        new RoleController(colony, roleView);
    }

    private void initInputProcessors() {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameScreen.getInputProcessor());
        multiplexer.addProcessor(structureController);
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

    @Override
    public void dispose() {
        gameScreen.dispose();
        skinAtlas.dispose();
        uiSkin.dispose();
    }

    @Override
    public void render() {
        super.render();
        colony.update();
        terrainController.update();
        colonyController.update();
        structureController.update();
        inventoryController.update();
    }

}
