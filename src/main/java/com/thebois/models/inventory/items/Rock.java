package com.thebois.models.inventory.items;

public class Rock implements IItem {

    @Override
    public ItemType getType() {
        return ItemType.ROCK;
    }

}
