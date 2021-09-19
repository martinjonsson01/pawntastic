package com.thebois.controllers;

import com.thebois.models.beings.roles.IRoleAllocator;
import com.thebois.models.beings.roles.RoleFactory;
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
    }

}
