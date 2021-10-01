package com.thebois.controllers;

import java.util.AbstractMap;
import java.util.stream.Collectors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.thebois.listeners.events.ValueChangedEvent;
import com.thebois.models.beings.roles.AbstractRole;
import com.thebois.models.beings.roles.IRoleAllocator;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.views.IActorView;
import com.thebois.views.RoleView;
import com.thebois.views.SpinnerButton;

/**
 * Controls the interactions from the player regarding role allocations.
 */
public class RoleController implements IController<IActorView> {

    private final IRoleAllocator roleAllocator;
    private final RoleView roleView;
    private final AbstractMap<RoleType, SpinnerButton> roleButtons;

    /**
     * Instantiates a new role controller that controls the provided role view.
     *
     * @param roleAllocator Used for getting the current role allocations.
     * @param skin          The skin to style widgets with.
     */
    public RoleController(final IRoleAllocator roleAllocator, final Skin skin) {
        this.roleAllocator = roleAllocator;
        this.roleView = new RoleView(skin);

        roleView.updateRoles(RoleFactory.all()
                                        .stream()
                                        .map(AbstractRole::getType)
                                        .filter(type -> !type.equals(RoleType.IDLE))
                                        .collect(Collectors.toList()));

        roleButtons = roleView.getRoleButtons();
        for (final RoleType roleType : roleButtons.keySet()) {
            final SpinnerButton button = roleButtons.get(roleType);
            button.registerListener(event -> onRoleButtonClick(roleType, event));
            button.setCanDecrease(roleCount -> roleAllocator.canDecreaseAllocation(roleType));
            button.setCanIncrease(roleCount -> roleAllocator.canIncreaseAllocation());
        }
    }

    private void onRoleButtonClick(final RoleType role, final ValueChangedEvent<Integer> event) {
        final int difference = event.getNewValue() - event.getOldValue();
        if (Math.abs(difference) > 1) {
            throw new IllegalCallerException("Can not allocate multiple IBeings at the same time");
        }
        switch (difference) {
            case 1 -> roleAllocator.tryIncreaseAllocation(role);
            case -1 -> roleAllocator.tryDecreaseAllocation(role);
            default -> throw new IllegalStateException("Expected a difference but there was none");
        }

        // Update disabled state of all role buttons whenever a role allocation is made.
        for (final SpinnerButton roleButton : roleButtons.values()) {
            roleButton.updateButtonDisabledState();
        }
    }

    @Override
    public IActorView getIView() {
        return roleView;
    }

    @Override
    public void update() {

    }

}
