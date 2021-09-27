package com.thebois.models.world.resources;

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

}
