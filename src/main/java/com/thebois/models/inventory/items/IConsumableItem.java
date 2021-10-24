package com.thebois.models.inventory.items;

/**
 * Represents an item that can be consumed by beings.
 *
 * @author Martin
 */
public interface IConsumableItem extends IItem {

    /**
     * Gets how many hunger points this item restores when consumed.
     *
     * @return How many hunger points are restored by consuming.
     */
    float getNutrientValue();

}
