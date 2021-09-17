package com.thebois.models.world;

import com.thebois.models.IDeepClonable;
import com.thebois.models.Position;

public abstract class Terrain implements ITerrain {

    private final Position position;

    protected Terrain(Position position) {
        this.position = position;
    }

    protected Terrain(float posX, float posY) {
        this.position = new Position(posX, posY);
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

}
