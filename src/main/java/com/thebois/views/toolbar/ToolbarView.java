package com.thebois.views.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;

import com.thebois.views.DrawableUtils;
import com.thebois.views.info.IActorView;

/**
 * The view that displays the different items in the toolbar.
 */
public class ToolbarView {

    private final Container<HorizontalGroup> toolbarPane;

    /**
     * Instantiates a new view of a toolbar that the player can interact with.
     *
     * @param widgetViews The items that should be displayed in the toolbar.
     */
    public ToolbarView(final Iterable<IActorView> widgetViews) {

        final HorizontalGroup toolbar = new HorizontalGroup();
        toolbar.expand().expand().fill();
        for (final IActorView actorView : widgetViews) {
            toolbar.addActor(actorView.getWidgetContainer());
        }

        toolbarPane = new Container<>(toolbar).fill();
        toolbarPane.setBackground(DrawableUtils.createPaneBackground());
    }

    public Actor getPane() {
        return toolbarPane;
    }

}
