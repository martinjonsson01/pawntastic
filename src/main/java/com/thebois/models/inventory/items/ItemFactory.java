package com.thebois.models.inventory.items;

/**
 * Creates items of specific types.
 *
 * @author Martin
 */
public final class ItemFactory {

    /**
     * How many hunger points are restored by eating an edible item.
     */
    private static final float NUTRIENT_VALUE = 10f;

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
        if (type.isEdible()) return new ConsumableItem(type, NUTRIENT_VALUE);
        return new Item(type);
    }

}
