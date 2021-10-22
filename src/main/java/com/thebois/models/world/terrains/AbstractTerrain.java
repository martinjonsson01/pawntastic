package com.thebois.models.world.terrains;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * Abstract implementation of ITerrain.
 *
 * @author Jonathan
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
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final AbstractTerrain terrain = (AbstractTerrain) other;
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
