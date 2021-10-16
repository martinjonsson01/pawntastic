package com.thebois.views.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;

import com.thebois.views.info.IActorView;

/**
 * The view that displays the different items in the toolbar.
 */
public class ToolbarView {

    private final HorizontalGroup toolbar;

    /**
     * Instantiates a new view of a toolbar that the player can interact with.
     *
     * @param widgetViews The items that should be displayed in the toolbar.
     */
    public ToolbarView(final Iterable<IActorView> widgetViews) {

        toolbar = new HorizontalGroup();
        toolbar.expand().fill();

        for (final IActorView actorView : widgetViews) {
            toolbar.addActor(actorView.getWidgetContainer());
        }
    }

    public Actor getPane() {
        return toolbar;
    }

}
