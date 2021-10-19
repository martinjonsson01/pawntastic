package com.thebois.views.debug;

import java.util.Collection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.Pawntastic;
import com.thebois.models.Position;
import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.IBeingGroup;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.IWorld;
import com.thebois.views.TextureUtils;
import com.thebois.views.game.IView;

/**
 * Recreates being paths by recalculating them, and then displays them.
 */
public class BeingPathDebugView implements IView {

    private static final int RADIUS = 2;
    private static final int TILE_SIZE = Pawntastic.getTileSize();
    private final IBeingGroup beingGroup;
    private final Texture circleTexture;
    private final IPathFinder pathFinder;

    /**
     * Initializes with a group of beings to display paths for.
     *
     * @param beingGroup The group containing the beings to display paths of.
     * @param world      The world to recalculate paths in.
     */
    public BeingPathDebugView(
        final IBeingGroup beingGroup, final IWorld world) {
        this.beingGroup = beingGroup;
        this.circleTexture = TextureUtils.createCircleTexture(RADIUS);
        this.pathFinder = new AstarPathFinder(world);
    }

    @Override
    public void dispose() {
        circleTexture.dispose();
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        for (final IBeing being : beingGroup.getBeings()) {
            batch.setColor(Color.RED);
            final Collection<Position> path = pathFinder.path(being.getPosition(),
                                                              being.getDestination());
            for (final Position position : path) {
                batch.draw(circleTexture,
                           position.getPosX() * TILE_SIZE + offsetX + TILE_SIZE / 2f,
                           position.getPosY() * TILE_SIZE + offsetY + TILE_SIZE / 2f);
            }
        }
    }

}
