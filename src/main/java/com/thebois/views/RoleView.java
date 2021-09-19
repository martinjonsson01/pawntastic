package com.thebois.views;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import com.thebois.models.beings.roles.IRoleAllocator;

/**
 * Displays all the different roles and the controls used to assign them.
 */
public class RoleView implements IActorView {

    private final Skin skin;
    private final IRoleAllocator roleAllocator;
    private final BitmapFont font;

    /**
     * The view needs an IRoleAllocator to render.
     *
     * @param skin          The skin to style widgets with
     * @param roleAllocator Used for getting the current role allocations
     * @param font          The font to render all text in
     */
    public RoleView(final Skin skin, final IRoleAllocator roleAllocator, final BitmapFont font) {
        this.skin = skin;
        this.roleAllocator = roleAllocator;
        this.font = font;
    }

    @Override
    public Actor getRoot() {
        final TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        final TextButton button1 = new TextButton("Button 1", buttonStyle);
        final TextButton button2 = new TextButton("Button 2", buttonStyle);
        final TextButton button3 = new TextButton("Button 3", buttonStyle);

        final VerticalGroup group = new VerticalGroup();
        group.addActor(button1);
        group.addActor(button2);
        group.addActor(button3);
        return group;
    }

}
