package com.thebois.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.IBeingGroup;

/**
 * Used to display all pawns.
 */
public class ColonyView implements IView {

    private final int radius = 5;
    private final Color beingColor = Color.WHITE;
    private final float tileSize;
    private IBeingGroup colony;
    private Texture beingTexture;

    /**
     * Instantiates a new ColonyView.
     *
     * @param tileSize The size of a single tile, in pixels.
     */
    public ColonyView(final float tileSize) {
        this.tileSize = tileSize;

        createBeingTexture();
    }

    private void createBeingTexture() {
        final Pixmap pixmap = new Pixmap(radius, radius, Pixmap.Format.RGBA8888);
        pixmap.setColor(beingColor);
        pixmap.fillCircle(0, 0, radius);
        beingTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Updates the views with current beings.
     *
     * @param beingGroup list of beings to draw.
     */
    public void update(IBeingGroup beingGroup) {
        this.colony = beingGroup;
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        float posX;
        float posY;

        if (colony != null) {
            for (IBeing pawn : colony.getBeings()) {
                posX = pawn.getPosition().getPosX() * tileSize;
                posY = pawn.getPosition().getPosY() * tileSize;

                batch.setColor(beingColor);
                batch.draw(beingTexture, offsetX + posX, offsetY + posY, radius, radius);
            }
        }
    }

    @Override
    public void dispose() {
        beingTexture.dispose();
    }

}
