package com.thebois.views;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.thebois.models.beings.roles.IRoleAllocator;

/**
 * Displays all the different roles and the controls used to assign them.
 */
public class RoleView implements IView {

    private final IRoleAllocator roleAllocator;
    private final BitmapFont font;

    /**
     * The view needs an IRoleAllocator to render.
     *
     * @param roleAllocator Used for getting the current role allocations
     * @param font          The font to render all text in
     */
    public RoleView(final IRoleAllocator roleAllocator, final BitmapFont font) {
        this.roleAllocator = roleAllocator;
        this.font = font;
    }

    @Override
    public void draw(final Batch batch, final float offsetX, final float offsetY) {
        //batch.rect(0, 0, 200, 200);
    }

}
