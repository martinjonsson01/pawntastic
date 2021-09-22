package com.thebois;

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

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    private static final int WORLD_SIZE = 50;
    /* These two decide the aspect ratio that will be preserved. */
    private static final float VIEWPORT_WIDTH = 1300;
    private static final float VIEWPORT_HEIGHT = 1000;
    private static final int DEFAULT_FONT_SIZE = 26;
    private static final int BEING_COUNT = 50;
    // LibGDX assets
    private BitmapFont font;
    private TextureAtlas atlas;
    private Skin skin;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private TerrainController terrainController;
    private RoleController roleController;
    private ColonyController colonyController;
    private World world;

    @Override
    public void create() {
        final float tileSize = Math.min(VIEWPORT_HEIGHT, VIEWPORT_WIDTH) / WORLD_SIZE;

        // Load LibGDX assets.
        generateFont();
        skin = new Skin();
        skin.add("default-font", font);
        atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin.addRegions(atlas);
        skin.load(Gdx.files.internal("uiskin.json"));

        // Models
        world = new World(WORLD_SIZE, BEING_COUNT);

        // Game Views
        final WorldView worldView = new WorldView(tileSize);
        final ColonyView colonyView = new ColonyView(tileSize);
        final List<IView> views = List.of(worldView, colonyView);
        final GameView gameView = new GameView(views, WORLD_SIZE, tileSize);
        // Info Views
        final RoleView roleView = new RoleView(skin);
        final List<IActorView> widgetViews = List.of(roleView);
        final InfoView infoView = new InfoView(widgetViews);

        // Screens
        gameScreen = new GameScreen(VIEWPORT_HEIGHT, VIEWPORT_WIDTH, skin, gameView, infoView);

        // Controllers
        this.terrainController = new TerrainController(world, worldView);
        this.roleController = new RoleController(world.getRoleAllocator(), roleView);
        this.colonyController = new ColonyController(world, colonyView);

        this.setScreen(gameScreen);
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
        atlas.dispose();
        skin.dispose();
    }

    @Override
    public void render() {
        super.render();
        world.update();
        terrainController.update();
        colonyController.update();
    }

}
