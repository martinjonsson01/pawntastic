package com.thebois.views;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
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
    /**
     * How many times bigger the textures should be relative to their displayed size.
     *
     * <p> E.g. a factor of 2 means the textures are created at 2x the resolution they are being
     * displayed at. </p>
     */
    private static final int TEXTURE_SUPER_SAMPLING_FACTOR = 8;

    static {
        ROLE_COLORS = new HashMap<>();
        ROLE_COLORS.put(RoleType.BUILDER, Color.LIGHT_GRAY);
        ROLE_COLORS.put(RoleType.FARMER, Color.GREEN);
        ROLE_COLORS.put(RoleType.FISHER, Color.BLUE);
        ROLE_COLORS.put(RoleType.GUARD, Color.BLACK);
        ROLE_COLORS.put(RoleType.LUMBERJACK, Color.BROWN);
        ROLE_COLORS.put(RoleType.MINER, Color.GRAY);
        ROLE_COLORS.put(RoleType.IDLE, Color.WHITE);
    }

    private final float tileSize;
    private final int radius;
    private IBeingGroup colony;
    private final Texture beingTexture;

    /**
     * Instantiates a new ColonyView.
     *
     * @param tileSize The size of a single tile, in pixels.
     */
    public ColonyView(final float tileSize) {
        this.tileSize = tileSize;
        this.radius = Math.round(tileSize / 2f);
        this.beingTexture = TextureUtils.createCircleTexture(radius
                                                             * TEXTURE_SUPER_SAMPLING_FACTOR);
    }

    /**
     * Updates the views with current beings.
     *
     * @param beingGroup list of beings to draw.
     */
    public void update(final IBeingGroup beingGroup) {
        this.colony = beingGroup;
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        float posX;
        float posY;

        if (colony != null) {
            for (final IBeing pawn : colony.getBeings()) {
                posX = pawn.getPosition().getPosX() * tileSize;
                posY = pawn.getPosition().getPosY() * tileSize;

                batch.setColor(ROLE_COLORS.get(pawn.getRole().getType()));

                batch.draw(beingTexture, offsetX + posX, offsetY + posY, radius * 2, radius * 2);
            }
        }
    }

    @Override
    public void dispose() {
        beingTexture.dispose();
    }

}
