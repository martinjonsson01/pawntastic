package com.thebois.controllers;

import java.util.AbstractMap;

import com.thebois.listeners.IEventSource;
import com.thebois.listeners.events.ValueChangedEvent;
import com.thebois.models.beings.roles.IRoleAllocator;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.roles.RoleType;
import com.thebois.views.RoleView;

/**
 * Controls the interactions from the player regarding role allocations.
 */
public class RoleController {

    private final IRoleAllocator roleAllocator;
    private final RoleView roleView;

    /**
     * Instantiates a new role controller that controls the provided role view.
     *
     * @param roleAllocator Used for getting the current role allocations
     * @param roleView      The view to listen to and send updates to
     */
    public RoleController(final IRoleAllocator roleAllocator, final RoleView roleView) {
        this.roleAllocator = roleAllocator;
        this.roleView = roleView;

        roleView.updateRoles(RoleFactory.all());

        final AbstractMap<RoleType, IEventSource<ValueChangedEvent<Integer>>> roleButtons =
            roleView.getRoleButtons();
        for (final RoleType role : roleButtons.keySet()) {
            final IEventSource<ValueChangedEvent<Integer>> button = roleButtons.get(role);
            button.addListener(event -> onRoleButtonClick(role, event));
        }
    }

    private void onRoleButtonClick(final RoleType role, final ValueChangedEvent<Integer> event) {
        final int difference = event.getNewValue() - event.getOldValue();
        if (Math.abs(difference) > 1) {
            throw new IllegalCallerException("Can not allocate multiple IBeings at the same time");
        }
        switch (difference) {
            case 1 -> {
                if (roleAllocator.tryIncreaseAllocation(role)) {
                    System.out.println("increased " + role.name());
                }
            }
            case -1 -> {
                if (roleAllocator.tryDecreaseAllocation(role)) {
                    System.out.println("decreased " + role.name());
                }
            }
            default -> throw new IllegalStateException("Expected a difference but there was none");
        }
    }

}
