package com.thebois.models.inventory.items;

public class Log implements IItem {

    @Override
    public ItemType getType() {
        return ItemType.LOG;
    }

}
