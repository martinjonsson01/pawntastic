package com.thebois.models.world;

import com.thebois.models.Position;

/**
 * Abstract implementation of ITerrain.
 */
public abstract class AbstractTerrain implements ITerrain {

    private final Position position;

    protected AbstractTerrain(Position position) {
        this.position = position;
    }

    protected AbstractTerrain(float posX, float posY) {
        this.position = new Position(posX, posY);
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

}
