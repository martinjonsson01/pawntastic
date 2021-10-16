package com.thebois.views.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.Pawntastic;
import com.thebois.models.Position;
import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.IBeingGroup;
import com.thebois.views.TextureUtils;
import com.thebois.views.game.IView;

/**
 * Displays the paths of all beings.
 */
public class BeingPathDebugView implements IView {

    private static final int RADIUS = 2;
    private static final int TILE_SIZE = Pawntastic.getTileSize();
    private final IBeingGroup beingGroup;
    private final Texture circleTexture;

    /**
     * Initializes with a group of beings to display paths for.
     *
     * @param beingGroup The group containing the beings to display paths of.
     */
    public BeingPathDebugView(final IBeingGroup beingGroup) {
        this.beingGroup = beingGroup;
        this.circleTexture = TextureUtils.createCircleTexture(RADIUS);
    }

    @Override
    public void dispose() {
        circleTexture.dispose();
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        for (final IBeing being : beingGroup.getBeings()) {
            batch.setColor(Color.RED);
            for (final Position position : being.getPath()) {
                batch.draw(
                    circleTexture,
                    position.getPosX() * TILE_SIZE + offsetX + TILE_SIZE / 2f,
                    position.getPosY() * TILE_SIZE + offsetY + TILE_SIZE / 2f);
            }
        }
    }

}
