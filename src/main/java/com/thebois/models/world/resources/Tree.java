package com.thebois.models.world.resources;

import com.thebois.models.Position;

/**
 * Resource of type tree.
 *
 * @author Mathias
 */
public class Tree extends AbstractResource {

    /**
     * Instantiate a new tree resource at given position.
     *
     * @param position The position where the tree resource should be created.
     */
    public Tree(final Position position) {
        super(position);
    }

    /**
     * Instantiate a new tree resource at given position.
     *
     * @param x X coordinate for the tree resource.
     * @param y Y coordinate for the tree resource.
     */
    public Tree(final float x, final float y) {
        this(new Position(x, y));
    }

    @Override
    public IResource deepClone() {
        return new Tree(getPosition());
    }

    @Override
    public float getCost() {
        return Float.MAX_VALUE;
    }

    @Override
    public ResourceType getType() {
        return ResourceType.TREE;
    }

}
