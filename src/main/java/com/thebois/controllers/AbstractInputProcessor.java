package com.thebois.controllers;

/**
 * A controller handling input from mouse and keyboard.
 */
public abstract class AbstractInputProcessor implements com.badlogic.gdx.InputProcessor {

    /**
     * Identifier for left click mouse button.
     */
    public static final int LEFT_CLICK = 0;

    @Override
    public boolean keyDown(final int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean touchDown(final int x, final int y, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchUp(final int x, final int y, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchDragged(final int x, final int y, final int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int x, final int y) {
        return false;
    }

    @Override
    public boolean scrolled(final float amountX, final float amountY) {
        return false;
    }

}
