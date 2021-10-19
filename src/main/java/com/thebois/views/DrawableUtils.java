package com.thebois.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Helpers for creating and using Drawables.
 */
public final class DrawableUtils {

    private static final Color PANE_BACKGROUND_COLOR = Color.valueOf("#0C2D48");

    private DrawableUtils() {

    }

    /**
     * Instantiates the drawable for pane backgrounds.
     *
     * @return The drawable.
     */
    public static Drawable createPaneBackground() {
        final Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(PANE_BACKGROUND_COLOR);
        bgPixmap.fill();
        return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
    }

}
