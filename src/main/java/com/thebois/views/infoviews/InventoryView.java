package com.thebois.views.infoviews;

import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import com.thebois.models.inventory.items.ItemType;
import com.thebois.utils.StringUtils;

/**
 * Information view for an inventory.
 */
public class InventoryView implements IActorView {

    private static final float PADDING = 20f;
    private final Skin skin;
    private final VerticalGroup root;

    /**
     * Instantiates the view that renders inventory info.
     *
     * @param skin The skin to style widgets with.
     */
    public InventoryView(final Skin skin) {
        this.skin = skin;

        root = new VerticalGroup().pad(PADDING);
        root.expand().fill();
    }

    @Override
    public Actor getWidgetContainer() {
        return root;
    }

    /**
     * When new count is received the view is will update the counters for all items.
     *
     * @param itemCount The count of each item in the inventory.
     */
    public void update(final Map<ItemType, Integer> itemCount) {
        root.clearChildren();
        final Label titleLabel = new Label("Inventory", skin);
        root.addActor(titleLabel);
        for (final var entry : itemCount.entrySet()) {
            final String itemName = entry.getKey().name().toLowerCase(Locale.ROOT);
            final String itemNameAndCount = StringUtils.capitalizeFirst(itemName + "s")
                                            + ": "
                                            + entry.getValue();
            final Label itemNameAndCountLabel = new Label(itemNameAndCount, skin);
            root.addActor(itemNameAndCountLabel);
        }
    }

}
