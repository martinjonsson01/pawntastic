package com.thebois.models.inventory.items;

/**
 * All the different types of items in the game.
 *
 * @author Jonathan
 * @author Martin
 */
public enum ItemType {
    /**
     * Logs are collected from trees.
     */
    LOG(10f, false),
    /**
     * Rocks are collected from stones.
     */
    ROCK(20f, false),
    /**
     * Fish can be caught from water.
     */
    FISH(3.0f, true);
    private final float weight;
    private final boolean edible;

    ItemType(final float weight, final boolean edible) {
        this.weight = weight;
        this.edible = edible;
    }

    /**
     * Gets the mass, described in kilograms.
     *
     * @return The mass in kilograms.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Gets whether the type is edible.
     *
     * @return Whether it is possible to eat.
     */
    public boolean isEdible() {
        return edible;
    }
}
