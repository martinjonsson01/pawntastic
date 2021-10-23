package com.thebois.models.world.resources;

import java.util.Objects;

import com.thebois.models.Position;

/**
 * A resource tile in the world, containing harvestable materials.
 *
 * @author Mathias
 */
public abstract class AbstractResource implements IResource {

    private final Position position;

    /**
     * Instantiate a resource with given position.
     *
     * @param position The position the resource should have.
     */
    protected AbstractResource(final Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        final AbstractResource that = (AbstractResource) object;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

}
