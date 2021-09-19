package com.thebois;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import org.mockito.Mockito;

import com.thebois.controllers.TerrainController;
import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.roles.Role;
import com.thebois.models.beings.roles.RoleAllocator;
import com.thebois.models.world.World;
import com.thebois.views.GameScreen;
import com.thebois.views.IView;
import com.thebois.views.RoleView;
import com.thebois.views.WorldView;

import static org.mockito.Mockito.*;

/**
 * The main representation of the game.
 */
class ColonyManagement extends Game {

    private static final int WORLD_SIZE = 50;
    private static final float VIEWPORT_WIDTH = 1920f;
    private static final float VIEWPORT_HEIGHT = 1080f;
    private BitmapFont font;
    // Screens
    private GameScreen gameScreen;
    private TerrainController terrainController;

    // Controllers

    @Override
    public void create() {
        final float tileSize = Math.min(VIEWPORT_HEIGHT, VIEWPORT_WIDTH) / WORLD_SIZE;

        // use libGDX's default Arial font
        font = new BitmapFont();

        // Models
        final World world = new World(WORLD_SIZE);
        final RoleAllocator roleAllocator = new RoleAllocator(mockBeings(10));

        // Views
        final WorldView worldView = new WorldView(tileSize);
        final RoleView roleView = new RoleView(roleAllocator, font);
        final ArrayList<IView> views = new ArrayList<>();
        views.add(worldView);
        views.add(roleView);

        // Screens
        gameScreen = new GameScreen(VIEWPORT_HEIGHT, VIEWPORT_WIDTH, WORLD_SIZE, tileSize, views);

        // Controllers
        this.terrainController = new TerrainController(world, worldView);

        this.setScreen(gameScreen);
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
        doAnswer(answer -> when(being.getRole()).thenReturn((Role) answer.getArguments()[0])).when(
            being).setRole(any());
        return being;
    }

    @Override
    public void dispose() {
        font.dispose();
        gameScreen.dispose();
    }

    @Override
    public void render() {
        super.render();
        terrainController.update();
    }

}
