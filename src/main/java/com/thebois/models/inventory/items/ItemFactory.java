package com.thebois.models.inventory.items;

/**
 * Creates items of specific types.
 */
public final class ItemFactory {

    private static final float NUTRIENT_VALUE_FISH = 10f;

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
            case FISH -> new ConsumableItem(ItemType.FISH, NUTRIENT_VALUE_FISH);
            default -> new Item(type);
        };
    }

}
