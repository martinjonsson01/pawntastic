package com.thebois.views.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.Position;
import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.IBeingGroup;
import com.thebois.views.IView;

/**
 * Displays the paths of all beings.
 */
public class BeingPathDebugView implements IView {

    private static final int RADIUS = 2;
    private final IBeingGroup beingGroup;
    private final float tileSize;
    private Texture circleTexture;

    /**
     * Initializes with a group of beings to display paths for.
     *
     * @param beingGroup The group containing the beings to display paths of.
     * @param tileSize   The size of a single tile in the world, in pixels.
     */
    public BeingPathDebugView(final IBeingGroup beingGroup, final float tileSize) {
        this.beingGroup = beingGroup;
        this.tileSize = tileSize;
        createCircleTexture();
    }

    private void createCircleTexture() {
        final Pixmap pixmap = new Pixmap(RADIUS * 2, RADIUS * 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(RADIUS, RADIUS, RADIUS);
        circleTexture = new Texture(pixmap, true);
        circleTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear,
                                Texture.TextureFilter.MipMapLinearLinear);
        pixmap.dispose();
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
                batch.draw(circleTexture,
                           position.getPosX() * tileSize + offsetX + tileSize / 2,
                           position.getPosY() * tileSize + offsetY + tileSize / 2);
            }
        }
    }

}
