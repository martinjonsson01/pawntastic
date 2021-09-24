package com.thebois.views.infoviews;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import com.thebois.models.inventory.items.IItem;

public class ColonyInventoryView {

    private final Skin skin;
    private final VerticalGroup root;
    private Iterable<IItem> items;

    /**
     * Instantiates the view that renders inventory info.
     *
     * @param skin The skin to style widgets with
     */
    public ColonyInventoryView(final Skin skin) {
        this.skin = skin;

        root = new VerticalGroup();
        root.expand().fill();
    }

    public update() {

    }

}
