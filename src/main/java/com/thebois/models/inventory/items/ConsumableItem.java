package com.thebois.models.inventory.items;

/**
 * An item that can be consumed for nutritional value.
 *
 * @author Martin
 */
class ConsumableItem extends Item implements IConsumableItem {

    private final float nutrientValue;

    /**
     * Instantiates with a given type and nutritional value.
     *
     * @param type          The type of the item.
     * @param nutrientValue How many hunger points should be restored when being consumed.
     */
    ConsumableItem(final ItemType type, final float nutrientValue) {
        super(type);
        this.nutrientValue = nutrientValue;
    }

    @Override
    public float getNutrientValue() {
        return nutrientValue;
    }

}
