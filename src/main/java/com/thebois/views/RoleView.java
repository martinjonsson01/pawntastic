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

    private static final float PADDING = 20f;
    private static final float BUTTON_PADDING = 5f;
    private final Skin skin;
    private final VerticalGroup root;
    private Iterable<AbstractRole> roles = new ArrayList<>();

    /**
     * The view needs an IRoleAllocator to render.
     *
     * @param skin The skin to style widgets with
     */
    public RoleView(final Skin skin) {
        this.skin = skin;

        root = new VerticalGroup();
        root.expand().fill();
        root.pad(PADDING, PADDING / 2f, 0f, PADDING / 2f);
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
            final Actor roleButton = createRoleButton(buttonStyle, role);
            root.addActor(roleButton);
        }
    }

    private Actor createRoleButton(final TextButton.TextButtonStyle buttonStyle,
                                   final AbstractRole role) {
        final TextButton button = new TextButton(role.getName(), buttonStyle);
        button.pad(BUTTON_PADDING);
        return button;
    }

    @Override
    public Actor getRoot() {
        createRoleButtons();
        return root;
    }

}
