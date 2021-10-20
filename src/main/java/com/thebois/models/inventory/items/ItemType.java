package com.thebois.models.inventory.items;

/**
 * All the different types of items in the game.
 */
public enum ItemType {
    /**
     * Logs are collected from trees.
     */
    LOG(3.0f),
    /**
     * Rocks are collected from stones.
     */
    ROCK(15.f),
    /**
     * Fish can be caught from water.
     */
    FISH(0.5f),
    /**
     * Wheat is harvested from wheat fields.
     */
    WHEAT(0.1f);
    private final float weight;

    ItemType(final float weight) {
        this.weight = weight;
    }

    /**
     * Gets the mass, described in kilograms.
     *
     * @return The mass in kilograms.
     */
    public float getWeight() {
        return weight;
    }
}
