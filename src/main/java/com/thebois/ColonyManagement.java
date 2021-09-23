package com.thebois;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.controllers.ColonyController;
import com.thebois.controllers.RoleController;
import com.thebois.controllers.TerrainController;
import com.thebois.models.world.World;
import com.thebois.views.ColonyView;
import com.thebois.views.GameScreen;
import com.thebois.views.GameView;
import com.thebois.views.IActorView;
import com.thebois.views.IView;
import com.thebois.views.InfoView;
import com.thebois.views.RoleView;
import com.thebois.views.WorldView;
import com.thebois.views.debug.BeingPathDebugView;

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
    /* Toggles debug-mode. */
    private static final boolean DEBUG = true;
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
    private WorldView worldView;
    private ColonyView colonyView;
    private BeingPathDebugView beingPathDebugView;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private TerrainController terrainController;
    private ColonyController colonyController;
    private float tileSize;

    @Override
    public void create() {
        tileSize = Math.min(VIEWPORT_HEIGHT, VIEWPORT_WIDTH) / WORLD_SIZE;

        setUpUserInterfaceSkin();

        setUpModelViewController();

        // Screens
        gameScreen = new GameScreen(VIEWPORT_HEIGHT, VIEWPORT_WIDTH, uiSkin, gameView, infoView);

        this.setScreen(gameScreen);
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

    private void setUpModelViewController() {
        createModels();

        if (DEBUG) createDebugView();
        createGameView();
        createInfoView();

        createControllers();
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

    private void createDebugView() {
        beingPathDebugView = new BeingPathDebugView(world.getColony(), tileSize);
    }

    private void createGameView() {
        worldView = new WorldView(tileSize);
        colonyView = new ColonyView(tileSize);
        final List<IView> views = new ArrayList<>();
        views.add(worldView);
        views.add(colonyView);
        if (DEBUG) {
            views.add(beingPathDebugView);
        }
        gameView = new GameView(views, WORLD_SIZE, tileSize);
    }

    private void createInfoView() {
        roleView = new RoleView(uiSkin);
        final List<IActorView> widgetViews = List.of(roleView);
        infoView = new InfoView(widgetViews);
    }

    private void createControllers() {
        this.terrainController = new TerrainController(world, worldView);
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
    }

}
