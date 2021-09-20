package com.thebois.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.models.beings.IBeing;
import com.thebois.models.beings.IBeingGroup;

/**
 * Used to display all pawns.
 */
public class ColonyView implements IView {

    private IBeingGroup colony;

    /**
     * Updates the views with current beings.
     *
     * @param beingGroup list of beings to draw.
     */
    public void update(IBeingGroup beingGroup) {
        this.colony = beingGroup;
    }

    @Override
    public void draw(final ShapeRenderer batch) {
        float posX;
        float posY;
        final float radius = 5;

        if (colony != null) {
            for (IBeing pawn : colony.getBeings()) {
                posX = pawn.getPosition().getPosX();
                posY = pawn.getPosition().getPosY();

                batch.setColor(Color.WHITE);
                batch.circle(posX, posY, radius);
            }
        }
    }

}
