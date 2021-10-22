package com.thebois.models.inventory.items;

/**
 * All the different types of items in the game.
 */
public enum ItemType {
    /**
     * Logs are collected from trees.
     */
    LOG(3.0f, false),
    /**
     * Rocks are collected from stones.
     */
    ROCK(15.f, false),
    /**
     * Fish can be caught from water.
     */
    FISH(0.5f, true),
    /**
     * Wheat is harvested from wheat fields.
     */
    WHEAT(0.1f, true);
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
