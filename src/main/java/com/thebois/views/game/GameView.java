package com.thebois.views.game;

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Disposable;

import com.thebois.Pawntastic;

/**
 * A view of the game world.
 */
public class GameView extends Widget implements Disposable {

    private final Collection<IView> views;
    private final float worldScreenSize;

    /**
     * Instantiates a new view of the world.
     *
     * @param views    The different sub-views of the world
     * @param tileSize The size of a single tile in world space
     */
    public GameView(final Collection<IView> views, final float tileSize) {
        this.views = views;
        this.worldScreenSize = tileSize * Pawntastic.WORLD_SIZE;
    }

    @Override
    public void dispose() {
        for (final Disposable view : views) {
            view.dispose();
        }
    }

    @Override
    public float getPrefHeight() {
        return getPrefWidth();
    }

    @Override
    public float getPrefWidth() {
        return worldScreenSize;
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        for (final IView view : views) {
            view.draw(batch, getX(), getY());
        }
    }

}
