package com.thebois.models.world;

import com.thebois.models.Position;

public class Grass extends Terrain {

    public Grass(float posX, float posY) {
        super(posX, posY);
    }

    public Grass(final Position position) {
        super(position);
    }

    @Override
    public TerrainType getType() {
        return TerrainType.GRASS;
    }

    @Override
    public Grass deepClone() {
        return new Grass(getPosition());
    }

}
