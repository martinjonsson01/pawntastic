package com.thebois.views;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Displays info about the Colony to the player and allows them to interact with it.
 */
public class InfoView {

    private final Container<VerticalGroup> infoPane;

    /**
     * Instantiates a new view displaying info for the player.
     *
     * @param widgetViews The widget views to render within this view
     * @param skin        The skin to style widgets with
     */
    public InfoView(final List<IActorView> widgetViews, final Skin skin) {

        final VerticalGroup infoPaneGroup = new VerticalGroup();
        for (final IActorView actorView : widgetViews) {
            infoPaneGroup.addActor(actorView.getRoot());
        }

        infoPane = new Container<>(infoPaneGroup).fill();
        infoPane.setBackground(createBackground(Color.BLUE));
    }

    private Drawable createBackground(final Color color) {
        final Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(color);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

    public Container<VerticalGroup> getPane() {
        return infoPane;
    }

}
