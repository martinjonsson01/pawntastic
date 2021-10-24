package com.thebois.views.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;

import com.thebois.views.DrawableUtils;
import com.thebois.views.info.IActorView;

/**
 * The view that displays the different items in the toolbar.
 *
 * @author Mathias
 */
public class ToolbarView implements IActorView {

    private final Container<HorizontalGroup> toolbarPane;

    /**
     * Instantiates with all the views that make the toolbar.
     *
     * @param widgetViews The views the toolbar consists of.
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

    @Override
    public Actor getWidgetContainer() {
        return toolbarPane;
    }

}
