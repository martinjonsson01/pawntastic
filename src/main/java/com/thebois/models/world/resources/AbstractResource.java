package com.thebois.models.world.resources;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * A resource tile in the world, containing harvestable materials.
 */
public abstract class AbstractResource implements IResource {

    private final Position position;

    /**
     * Instantiate a resource with given position.
     *
     * @param position The position the resource should have.
     */
    public AbstractResource(final Position position) {
        this.position = position;
    }

    /**
     * Instantiate a resource with given position.
     *
     * @param x X coordinate for the resource.
     * @param y Y Coordinate for the resource
     */
    public AbstractResource(final float x, final float y) {
        this(new Position(x, y));
    }

    @Override
    public Position getPosition() {
        return position.deepClone();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractResource that = (AbstractResource) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

}
