package com.thebois.views;

import java.util.Collection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.thebois.models.beings.IBeing;

/**
 * Used to display all pawns.
 */
public class ColonyView {

    /**
     * Draws all pawns.
     *
     * @param listOfPawns list of pawns to draw.
     * @param batch       used to draw.
     */
    public void drawAllPawns(ShapeRenderer batch, Collection<IBeing> listOfPawns) {

        float posX;
        float posY;
        final float radius = 5;

        batch.begin(ShapeRenderer.ShapeType.Filled);

        for (IBeing pawn : listOfPawns) {
            posX = pawn.getPosition().getPosX();
            posY = pawn.getPosition().getPosY();

            batch.setColor(Color.WHITE);
            batch.circle(posX, posY, radius);
        }
        batch.end();
    }

}
