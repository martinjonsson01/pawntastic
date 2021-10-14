package com.thebois.models.inventory.items;

/**
 * Creates items of specific types.
 */
public final class ItemFactory {

    private ItemFactory() {

    }

    /**
     * Creates an item from the provided type.
     *
     * @param type The type of item to create.
     *
     * @return The created item.
     */
    public static IItem fromType(final ItemType type) {
        return switch (type) {
            case LOG -> new Log();
            case ROCK -> new Rock();
        };
    }

}
