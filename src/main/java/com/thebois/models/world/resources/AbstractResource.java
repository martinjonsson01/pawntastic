package com.thebois.models.world.resources;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * Abstract implementation of IResource.
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
     * @param posX X coordinate for the resource.
     * @param posY Y Coordinate for the resource
     */
    public AbstractResource(final float posX, final float posY) {
        this.position = new Position(posX, posY);
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
        return Objects.equals(position, that.position) && Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, getType());
    }

}
