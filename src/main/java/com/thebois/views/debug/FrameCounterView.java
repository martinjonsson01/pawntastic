package com.thebois.views.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.thebois.views.game.IView;

/**
 * Displays an FPS counter on screen.
 *
 * @author Martin
 */
public class FrameCounterView implements IView {

    private final BitmapFont font;

    /**
     * Initializes with a font to render text in.
     *
     * @param font The font to render the counter in.
     */
    public FrameCounterView(final BitmapFont font) {
        this.font = font;
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        font.draw(batch, "FPS=" + Gdx.graphics.getFramesPerSecond(), offsetX, font.getLineHeight());
    }

    @Override
    public void dispose() {

    }

}
