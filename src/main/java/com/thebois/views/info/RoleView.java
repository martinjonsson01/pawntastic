package com.thebois.views.info;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import com.thebois.models.beings.roles.RoleType;
import com.thebois.utils.StringUtils;

/**
 * Displays all the different roles and the controls used to assign them.
 */
public class RoleView implements IActorView {

    private static final float PADDING = 20f;
    private static final float BUTTON_PADDING = 5f;
    private final VerticalGroup root;
    private final TextButton.TextButtonStyle buttonStyle;
    private final Skin skin;
    private final AbstractMap<RoleType, SpinnerButton> roleButtons = new HashMap<>();
    private Iterable<RoleType> roles = new ArrayList<>();

    /**
     * The view needs an IRoleAllocator to render.
     *
     * @param skin The skin to style widgets with.
     */
    public RoleView(final Skin skin) {
        this.skin = skin;

        root = new VerticalGroup().space(BUTTON_PADDING * 2f);
        root.expand().fill();
        root.pad(PADDING, PADDING / 2f, 0f, PADDING / 2f);

        buttonStyle = skin.get(TextButton.TextButtonStyle.class);
    }

    /**
     * Updates the roles to render allocation options for.
     *
     * @param newRoles The new list of roles.
     */
    public void updateRoles(final Iterable<RoleType> newRoles) {
        this.roles = newRoles;
        createRoleButtons();
    }

    private void createRoleButtons() {
        root.clearChildren();
        for (final RoleType roleType : roles) {
            final Actor roleButton = createRoleButton(roleType);
            root.addActor(roleButton);
        }
    }

    private Actor createRoleButton(final RoleType roleType) {
        final SpinnerButton button = new SpinnerButton(skin, 0, 99);
        button.pad(0f, BUTTON_PADDING, BUTTON_PADDING, BUTTON_PADDING);

        roleButtons.put(roleType, button);

        final String roleName = StringUtils.capitalizeFirst(roleType.name().toLowerCase(Locale.ROOT)
                                                            + "s");
        final Label roleLabel = new Label(roleName, skin);

        final Group buttonGroup = new VerticalGroup();
        buttonGroup.addActor(roleLabel);
        buttonGroup.addActor(button);
        return buttonGroup;
    }

    public AbstractMap<RoleType, SpinnerButton> getRoleButtons() {
        return roleButtons;
    }

    @Override
    public Actor getWidgetContainer() {
        return root;
    }

}
