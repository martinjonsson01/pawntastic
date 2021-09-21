package com.thebois.views;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.IBeingGroup;
import com.thebois.models.beings.roles.RoleType;

/**
 * Used to display all pawns.
 */
public class ColonyView implements IView {

    private static final Map<RoleType, Color> ROLE_COLORS;
    static {
        ROLE_COLORS = new HashMap<>();
        ROLE_COLORS.put(RoleType.BUILDER, Color.LIGHT_GRAY);
        ROLE_COLORS.put(RoleType.FARMER, Color.GREEN);
        ROLE_COLORS.put(RoleType.FISHER, Color.BLUE);
        ROLE_COLORS.put(RoleType.GUARD, Color.BLACK);
        ROLE_COLORS.put(RoleType.LUMBERJACK, Color.BROWN);
        ROLE_COLORS.put(RoleType.MINER, Color.GRAY);
    }
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
        final Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(beingColor);
        pixmap.fillCircle(radius, radius, radius);
        beingTexture = new Texture(pixmap);
        beingTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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

                if (pawn.getRole() != null) {
                    batch.setColor(ROLE_COLORS.get(pawn.getRole().getType()));
                }
                else {
                    batch.setColor(Color.WHITE);
                }

                batch.draw(beingTexture, offsetX + posX, offsetY + posY, radius * 2, radius * 2);
            }
        }
    }

    @Override
    public void dispose() {
        beingTexture.dispose();
    }

}
