package com.thebois.models.world.resources;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

/**
 * Resource of type tree.
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
     * @param posX X coordinate for the tree resource.
     * @param posY Y coordinate for the tree resource.
     */
    public Tree(final float posX, final float posY) {
        super(posX, posY);
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

    @Override
    public IItem harvest() {
        return ItemFactory.fromType(ItemType.LOG);
    }

}
