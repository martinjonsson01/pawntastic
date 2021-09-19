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

import org.mockito.Mockito;

import com.thebois.controllers.TerrainController;
import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.roles.Role;
import com.thebois.models.beings.roles.RoleAllocator;
import com.thebois.models.world.World;
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
    private static final float VIEWPORT_WIDTH = 1200;
    private static final float VIEWPORT_HEIGHT = 1000;
    private static final int DEFAULT_FONT_SIZE = 30;
    // LibGDX assets
    private BitmapFont font;
    private TextureAtlas atlas;
    private Skin skin;
    // Screens
    private GameScreen gameScreen;
    // Controllers
    private TerrainController terrainController;

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
        final World world = new World(WORLD_SIZE);
        final RoleAllocator roleAllocator = new RoleAllocator(mockBeings(10));

        // Game Views
        final WorldView worldView = new WorldView(tileSize);
        final List<IView> views = List.of(worldView);
        final GameView gameView = new GameView(views, WORLD_SIZE, tileSize);
        // Info Views
        final List<IActorView> widgetViews = List.of(new RoleView(skin, roleAllocator, font));
        final InfoView infoView = new InfoView(widgetViews, skin);

        // Screens
        gameScreen = new GameScreen(VIEWPORT_HEIGHT, VIEWPORT_WIDTH, skin, gameView, infoView);

        // Controllers
        this.terrainController = new TerrainController(world, worldView);

        this.setScreen(gameScreen);
    }

    private void generateFont() {
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(
            "fonts/arial.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter
            parameter =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = DEFAULT_FONT_SIZE;
        font = generator.generateFont(parameter);
        // To smooth out the text.
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,
                                                Texture.TextureFilter.Linear);
        generator.dispose();
    }

    /* temporary until 9-pawns is merged */
    private List<IBeing> mockBeings(final int beingCount) {
        final ArrayList<IBeing> beings = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            beings.add(mockBeing());
        }
        return beings;
    }

    /* temporary until 9-pawns is merged */
    private IBeing mockBeing() {
        final IBeing being = Mockito.mock(IBeing.class);
        // Mocks the getter and setter to simulate a real implementation.
        Mockito.doAnswer(answer -> {
            return Mockito.when(being.getRole()).thenReturn((Role) answer.getArguments()[0]);
        }).when(being).setRole(Mockito.any());
        return being;
    }

    @Override
    public void dispose() {
        font.dispose();
        atlas.dispose();
        skin.dispose();
        gameScreen.dispose();
    }

    @Override
    public void render() {
        super.render();
        terrainController.update();
    }

}
