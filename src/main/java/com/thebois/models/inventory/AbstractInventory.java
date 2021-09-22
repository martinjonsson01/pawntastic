package com.thebois.models.inventory;

import java.util.ArrayList;

public abstract class AbstractInventory {

    private final ArrayList<IItem> itemHolder = new ArrayList<>();

    public void addItem(final IItem item) {
        itemHolder.add(item);
    }

    public void removeItem(final IItem item) {
        itemHolder.remove(item);
    }

}
