package com.thebois.views.info;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import com.thebois.views.DrawableUtils;

/**
 * Displays info about the Colony to the player and allows them to interact with it.
 */
public class InfoView implements IActorView {

    private final Container<VerticalGroup> infoPane;

    /**
     * Instantiates a new view displaying info for the player.
     *
     * @param widgetViews The widget views to render within this view
     */
    public InfoView(final Iterable<IActorView> widgetViews) {

        final VerticalGroup infoPaneGroup = new VerticalGroup();
        infoPaneGroup.expand().fill();

        for (final IActorView actorView : widgetViews) {
            infoPaneGroup.addActor(actorView.getWidgetContainer());
        }

        infoPane = new Container<>(infoPaneGroup).fill();
        infoPane.setBackground(DrawableUtils.createPaneBackground());
    }

    @Override
    public Actor getWidgetContainer() {
        return infoPane;
    }

}
