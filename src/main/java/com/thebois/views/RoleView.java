package com.thebois.views;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import com.thebois.models.beings.roles.AbstractRole;

/**
 * Displays all the different roles and the controls used to assign them.
 */
public class RoleView implements IActorView {

    private final Skin skin;
    private Iterable<AbstractRole> roles = new ArrayList<>();
    private VerticalGroup root;

    /**
     * The view needs an IRoleAllocator to render.
     *
     * @param skin The skin to style widgets with
     */
    public RoleView(final Skin skin) {
        this.skin = skin;
    }

    /**
     * Updates the roles to render allocation options for.
     *
     * @param newRoles The new list of roles.
     */
    public void updateRoles(final Iterable<AbstractRole> newRoles) {
        this.roles = newRoles;
        createRoleButtons();
    }

    private void createRoleButtons() {
        root.clearChildren();
        final TextButton.TextButtonStyle buttonStyle = skin.get(TextButton.TextButtonStyle.class);
        for (final AbstractRole role : roles) {
            final TextButton roleButton = new TextButton(role.getName(), buttonStyle);
            root.addActor(roleButton);
        }
    }

    @Override
    public Actor getRoot() {
        root = new VerticalGroup();
        createRoleButtons();
        return root;
    }

}
