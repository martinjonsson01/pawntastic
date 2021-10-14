package com.thebois.models.world.terrains;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * Abstract implementation of ITerrain.
 */
public abstract class AbstractTerrain implements ITerrain {

    private final Position position;

    protected AbstractTerrain(final Position position) {
        this.position = position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractTerrain terrain = (AbstractTerrain) o;
        return Objects.equals(position, terrain.position);
    }

    @Override
    public String toString() {
        return "AbstractTerrain{" + "position=" + position + '}';
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

}
